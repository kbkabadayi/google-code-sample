package com.google;

import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist implements Comparable<VideoPlaylist> {
    private String name;
    private ArrayList<Video> videos;
    private VideoLibrary videoLibrary;

    public VideoPlaylist(String name, VideoLibrary vl) {
        videoLibrary = vl;
        this.name = name;
        videos = new ArrayList<>();
    }

    public boolean addVideoToPlaylist(String videoId) {
        if (!videoLibrary.getVideoIDs().contains(videoId)) {
            return false;
        }
        Video video = videoLibrary.getVideo(videoId);
        if (videos.contains(video)) {
            return false;
        }
        videos.add(video);
        return true;
    }
    
    public void removeVideo(String videoId) {
        videos.remove(videoLibrary.getVideo(videoId));
    }

    public void clearList() {
        videos.clear();
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (videos.isEmpty()) {
            sb.append("No videos here yet \n");
            return sb.toString();
        }
        for (Video video : videos) {
            sb.append("  " + video + "\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof VideoPlaylist)) {
            throw new IllegalArgumentException();
        }
        VideoPlaylist thatPlayList = (VideoPlaylist) obj;
        return this.name.equals(thatPlayList.name);
    }

    @Override
    public int hashCode() {
        int k = 31;
        int result = 0;
        for (int i = 0; i < name.length(); i++) {
            result += k * (i + 1) * name.charAt(i);
        }
        return result;
    }

    @Override
    public int compareTo(VideoPlaylist o) {
        return this.name.compareTo(o.name);
    }

   

}
