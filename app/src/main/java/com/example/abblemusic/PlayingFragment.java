package com.example.abblemusic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import static com.example.abblemusic.R.drawable.ic_baseline_pause_24;


public class PlayingFragment extends Fragment {

    public ImageView albumphoto;
    public TextView songname;
    public ImageButton prev,play,next;

    public PlayingFragment() {
        // Required empty public constructor
    }

    //this is done to insure that the method is implemented in the used activity for this fragment
    PlayingFragmentListener activityControl;
    public interface PlayingFragmentListener{
        public void next();
        public void prev();
    }

    //this is called whenever the fragment is attached to an activity

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityControl = (PlayingFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setUpImageLoader();
        View view = inflater.inflate(R.layout.fragment_playing, container, false);
        albumphoto = view.findViewById(R.id.albumphoto);
        songname = view.findViewById(R.id.songname);
        prev = view.findViewById(R.id.prevalbum);
        prev.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                activityControl.prev();
            }
        });
        next = view.findViewById(R.id.nextalbum);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityControl.next();
            }
        });
        return view;
    }

    public void setSong(String image, String name){
        int defaultImage = this.getResources().getIdentifier(image,null, getContext().getPackageName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();
        imageLoader.displayImage(image,albumphoto,options);
        songname.setText(name);
    }

//    public void changeIcon(Boolean check){
//        if (check) {
//            //play.setImageDrawable();
//        }
//    }

    private void setUpImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

}