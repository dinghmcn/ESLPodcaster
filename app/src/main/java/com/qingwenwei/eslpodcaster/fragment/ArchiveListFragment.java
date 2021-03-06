package com.qingwenwei.eslpodcaster.fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingwenwei.eslpodcaster.R;
import com.qingwenwei.eslpodcaster.adapter.ArchiveListAdapter;
import com.qingwenwei.eslpodcaster.constant.Constants;
import com.qingwenwei.eslpodcaster.db.EpisodeDAO;
import com.qingwenwei.eslpodcaster.entity.PodcastEpisode;
import com.qingwenwei.eslpodcaster.event.OnEpisodeListRefreshEvent;
import com.qingwenwei.eslpodcaster.event.OnLoadPlayingEpisodeEvent;
import com.qingwenwei.eslpodcaster.util.EpisodeStatusUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ArchiveListFragment extends Fragment
        implements View.OnLongClickListener, View.OnClickListener{

    private static final String TAG = "ArchiveListFragment";

    private RecyclerView recyclerView;
    private ArchiveListAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        adapter = new ArchiveListAdapter();
        adapter.setOnCardViewLongClickListener(this);
        adapter.setOnCardViewClickListener(this);

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_archives, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public boolean onLongClick(View v) {
        //show dialog
        switch (v.getId()){
            case R.id.archiveCardView:
                showPopupMenu((PodcastEpisode) v.getTag());
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //load playing episode
            case R.id.archiveCardView:
                PodcastEpisode episode = (PodcastEpisode) v.getTag();
                EventBus.getDefault().post(new OnLoadPlayingEpisodeEvent(episode));
                break;
        }
    }

    @Subscribe
    public void refresh(OnEpisodeListRefreshEvent event){
        if(event.message.equals(Constants.ON_ARCHIVED_EPISODE_LIST_REFRESH_EVENT))
            new RefreshArchivedEpisodeListAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class RefreshArchivedEpisodeListAsyncTask
            extends AsyncTask<Void, Void, ArrayList<PodcastEpisode>>{
        private static final String TAG = "RefreshArchivedEpisodeListAsyncTask";

        @Override
        protected ArrayList<PodcastEpisode> doInBackground(Void... params) {
            EpisodeDAO dao = new EpisodeDAO(getContext());
            ArrayList archives = (ArrayList) dao.getAllArchivedEpisodes();
            return archives;
        }

        @Override
        protected void onPostExecute(ArrayList<PodcastEpisode> podcastEpisodes) {
            Log.i(TAG,"Refreshed archive list size: " + podcastEpisodes.size());
            if (podcastEpisodes.size() == 0) podcastEpisodes.add(null);
            adapter.updateEpisodes(podcastEpisodes);
        }
    }

    private void showPopupMenu(final PodcastEpisode episode){
        CharSequence items[] = new CharSequence[] {
            "Download this episode",
            "Unarchive this episode"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,episode.getTitle() + " which:" + which);
                switch (which){
                    //download
                    case 0:{
                        EpisodeStatusUtil.downloadEpisode(episode, getContext());
                        break;
                    }

                    //unarchive
                    case 1:{
                        EpisodeStatusUtil.unarchiveEpisode(episode, getContext());
                        EventBus.getDefault().post(
                                new OnEpisodeListRefreshEvent(
                                        Constants.ON_ARCHIVED_EPISODE_LIST_REFRESH_EVENT));
                        break;
                    }
                }
            }
        });
        builder.show();
    }

}
