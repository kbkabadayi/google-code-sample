package com.google;

import java.util.Collections;
import java.util.List;


/** A class used to represent a video. */
class Video  implements Comparable<Video> {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean flagged;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public boolean setFlag(String reason) {
    if (flagged) {
      return false;
    }

    if (reason.equals("")) {
      flagReason = "Not supplied";
    } else {
      flagReason = reason;
    }
    flagged = true;
    return true;
  }

  public boolean unflag() {
    if (!flagged) {
      return false;
    }

    flagged = false;
    flagReason = "";
    return true;
  }

  public boolean isFlagged() {
    return flagged;
  }

  public String getReason() {
    return flagReason;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(title + " (" + videoId + ") ["); 
    for (String tag : tags) {
      sb.append(tag + " ");
    }
    if (!(sb.substring(sb.length() - 1).equals("["))) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    if (flagged) {
      sb.append(" - FLAGGED (reason: " + flagReason + ")");
    }
    return sb.toString();
  }

  @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Video)) {
            throw new IllegalArgumentException();
        }
        Video video = (Video) obj;
        return this.title.equals(video.title);
    }

  public static void main(String[] args) {
    
  }

  @Override
  public int compareTo(Video other) {
    return this.title.compareTo(other.title);
  }
}
