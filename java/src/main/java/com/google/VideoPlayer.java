package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class VideoPlayer {
  private final Locale enLocale = new Locale("en", "US");
  private final VideoLibrary videoLibrary;
  private final HashMap<String, VideoPlaylist> nameToPlaylist;
  private boolean videoPlaying;
  private boolean videoPaused;
  private int availableIdNum;
  private String currentVidID;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    nameToPlaylist = new HashMap<>();
    availableIdNum = videoLibrary.getVideos().size();
    currentVidID = "";
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> videos = videoLibrary.getVideos();
    Collections.sort(videos);
    StringBuilder sb = new StringBuilder("Here's a list of all available videos: \n");
    for (Video video : videos) {
      sb.append("  " + video + "\n");
    }
    System.out.println(sb.toString());
  }

  public void playVideo(String videoId) {
    if (!videoLibrary.getVideoIDs().contains(videoId)) {
      System.out.println("Cannot play video: Video does not exist");
      return;
    }

    if (videoLibrary.getVideo(videoId).isFlagged()) {
      Video video = videoLibrary.getVideo(videoId);
      String reason = video.getReason();
      System.out.println("Cannot play video: Video is currently flagged (reason: " + reason + ")");
      return;
    }

    StringBuilder sb = new StringBuilder();
    if (videoPlaying) {
      sb.append("Stopping video: " + videoLibrary.getVideo(currentVidID).getTitle() + "\n");
    }
    currentVidID = videoId;
    sb.append("Playing video: " + videoLibrary.getVideo(currentVidID).getTitle());
    videoPlaying = true;
    videoPaused = false;
    System.out.println(sb.toString());

  }

  public void stopVideo() {
    if (!videoPlaying) {
      System.out.println("Cannot stop video: No video is currently playing");
      return;
    }

    StringBuilder sb = new StringBuilder("Stopping video: " + videoLibrary.getVideo(currentVidID).getTitle());
    currentVidID = "";
    videoPlaying = false;
    System.out.println(sb.toString());
  }

  public void playRandomVideo() {
    int videosSize = availableIdNum;

    if (videosSize == 0) {
      System.out.println("No videos available.");
      return;
    }

    StringBuilder sb = new StringBuilder();
    if (videoPlaying) {
      sb.append("Stopping video: " + videoLibrary.getVideo(currentVidID).getTitle() + "\n");
    }
    int randIndex = (int) (Math.random() * videosSize);

    currentVidID = videoLibrary.getVideoIDs().get(randIndex);

    videoPlaying = true;
    videoPaused = false;

    sb.append("Playing video: " + videoLibrary.getVideo(currentVidID).getTitle());
    System.out.println(sb.toString());
  }

  public void pauseVideo() {
    if (!videoPlaying) {
      System.out.println("Cannot pause video: No video is currently playing.");
      return;
    }
    StringBuilder sb = new StringBuilder();
    if (videoPaused) {
      sb.append("Video already paused: "); 
    } else {
      sb.append("Pausing video: ");
    }
    videoPaused = true;
    sb.append(videoLibrary.getVideo(currentVidID).getTitle());
    System.out.println(sb.toString());
  }

  public void continueVideo() {
    if (!videoPlaying) {
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }
    if (!videoPaused) {
      System.out.println("Cannot continue video: Video is not paused");
      return;
    }

    System.out.println("Continuing video: " + videoLibrary.getVideo(currentVidID).getTitle());
    videoPaused = false;
  }

  public void showPlaying() {
    if (!videoPlaying) {
      System.out.println("No video is currently playing");
      return;
    }
    StringBuilder sb = new StringBuilder("Currently playing: " + videoLibrary.getVideo(currentVidID));
    if (videoPaused) {
      sb.append(" - PAUSED");
    }
    
    System.out.println(sb.toString());
  }

  public void createPlaylist(String playlistName) {
    if (nameToPlaylist.containsKey(playlistName.toLowerCase(enLocale))) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
      return;
    }
    VideoPlaylist playlist = new VideoPlaylist(playlistName, videoLibrary);
    nameToPlaylist.put(playlistName.toLowerCase(enLocale), playlist);
    System.out.println("Successfully created new playlist: " + playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    if (!nameToPlaylist.containsKey(playlistName.toLowerCase(enLocale))) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      return;
    }

    if (!videoLibrary.getVideoIDs().contains(videoId)) {
      System.out.println("Cannot add video to " + playlistName +  ": Video does not exist");
      return;
    }

    if (videoLibrary.getVideo(videoId).isFlagged()) {
      Video video = videoLibrary.getVideo(videoId);
      System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + video.getReason() + ")");
      return;
    }

    VideoPlaylist playlist = nameToPlaylist.get(playlistName.toLowerCase(enLocale));

    if (playlist.addVideoToPlaylist(videoId)) {
      System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
    } else {
      System.out.println("Cannot add video to " + playlistName + ": Video already added");
    }
    
  }

  public void showAllPlaylists() {
    if (nameToPlaylist.keySet().isEmpty()) {
      System.out.println("No playlists exist yet");
      return;
    }
    StringBuilder sb = new StringBuilder("Showing all playlists: \n");
    ArrayList<VideoPlaylist> playlistsList = new ArrayList<>();
    for (VideoPlaylist playlist : nameToPlaylist.values()) {
      playlistsList.add(playlist);
    }
    Collections.sort(playlistsList);
    for (VideoPlaylist playlist : playlistsList) {
      sb.append("  " + playlist.getName() + "\n");
    }
    System.out.print(sb.toString());
  }

  public void showPlaylist(String playlistName) {
    if (!nameToPlaylist.containsKey(playlistName.toLowerCase(enLocale))) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    StringBuilder sb = new StringBuilder("Showing playlist: " + playlistName + "\n");
    sb.append(nameToPlaylist.get(playlistName.toLowerCase(enLocale)));
    System.out.print(sb.toString());
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if (!nameToPlaylist.containsKey(playlistName.toLowerCase(enLocale))) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
      return;
    }
    if (!videoLibrary.getVideoIDs().contains(videoId)) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      return;
    }
    VideoPlaylist playlist = nameToPlaylist.get(playlistName.toLowerCase(enLocale));
    if (!playlist.getVideos().contains(videoLibrary.getVideo(videoId))) {
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      return;
    }
    playlist.removeVideo(videoId);
    System.out.println("Removed video from " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
  }

  public void clearPlaylist(String playlistName) {
    if (!nameToPlaylist.containsKey(playlistName.toLowerCase(enLocale))) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    VideoPlaylist playlist = nameToPlaylist.get(playlistName.toLowerCase(enLocale));
    playlist.clearList();
    System.out.println("Successfully removed all videos from " + playlistName);
  }

  public void deletePlaylist(String playlistName) {
    if (!nameToPlaylist.containsKey(playlistName.toLowerCase(enLocale))) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
      return;
    }
    nameToPlaylist.remove(playlistName.toLowerCase(enLocale));
    System.out.println("Deleted playlist: " + playlistName);
  }

  public void searchVideos(String searchTerm) {
    List<String> hitTerms = new ArrayList<>();
    for (String id : videoLibrary.getVideoIDs()) {
      if (id.contains(searchTerm.toLowerCase(enLocale))) {
        if (videoLibrary.getVideo(id).isFlagged()) {
          continue;
        }
        hitTerms.add(id);
      }
    }
    if (hitTerms.isEmpty()) {
      System.out.println("No search results for " + searchTerm);
      return;
    }
    StringBuilder sb = new StringBuilder("Here are the results for " + searchTerm + ":\n");
    Collections.sort(hitTerms);
    int i = 1;
    for (String id : hitTerms) {
      sb.append("  " + i + ") " + videoLibrary.getVideo(id) + "\n");
      i++;
    }
    sb.append("Would you like to play any of the above? If yes, specify the number of the video." + "\n" +
     "If your answer is not a valid number, we will assume it's a no.");
     System.out.println(sb.toString());
     Scanner scan = new Scanner(System.in);
     try {
      int num = scan.nextInt();
      playVideo(hitTerms.get(num - 1));
     } catch (Exception e) {

     }
     scan.close();
  }

  public void searchVideosWithTag(String videoTag) {
    List<String> hitTerms = new ArrayList<>();
    for (Video video : videoLibrary.getVideos()) {
      if (video.getTags().contains(videoTag.toLowerCase(enLocale))) {
        if (video.isFlagged()) {
          continue;
        }
        hitTerms.add(video.getVideoId());
      }
    }
    if (hitTerms.isEmpty()) {
      System.out.println("No search results for " + videoTag);
      return;
    }
    StringBuilder sb = new StringBuilder("Here are the results for " + videoTag + ":\n");
    Collections.sort(hitTerms);
    int i = 1;
    for (String id : hitTerms) {
      sb.append("  " + i + ") " + videoLibrary.getVideo(id) + "\n");
      i++;
    }
    sb.append("Would you like to play any of the above? If yes, specify the number of the video." + "\n" +
     "If your answer is not a valid number, we will assume it's a no.");
     System.out.println(sb.toString());
     Scanner scan = new Scanner(System.in);
     try {
      int num = scan.nextInt();
      playVideo(hitTerms.get(num - 1));
     } catch (Exception e) {

     }
     scan.close();
  }

  public void flagVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }
    if (videoPlaying && videoLibrary.getVideo(currentVidID).equals(video)) {
      stopVideo();
    }  
    if (video.setFlag("")) {
      System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: Not supplied)");
      availableIdNum--;
    }
    else {
      System.out.println("Cannot flag video: Video is already flagged");
    }
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }
    if (videoPlaying && videoLibrary.getVideo(currentVidID).equals(video)) {
      stopVideo();
    }
    if (video.setFlag(reason)) {
      System.out.println("Successfully flagged video: " + videoLibrary.getVideo(videoId).getTitle() + " (reason: " + reason + ")");
      availableIdNum--;
    }
    else {
      System.out.println("Cannot flag video: Video is already flagged");
    }
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot remove flag from video: Video does not exist");
      return;
    }

    if (video.unflag()) {
      System.out.println("Successfully removed flag from video: " + video.getTitle());
    }
    else {
      System.out.println("Cannot remove flag from video: Video is not flagged");
    }
    availableIdNum++;
  }

  public static void main(String[] args) {
    VideoPlayer videoPlayer = new VideoPlayer();

    videoPlayer.flagVideo("video_does_not_exist", "flagVideo_reason");

  }
}