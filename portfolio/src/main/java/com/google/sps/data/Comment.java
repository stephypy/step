// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

/** Class representing the template for a comment (nickname + comment content) */
public class Comment {
  private String nickname;
  private String commentContent;
  private double sentimentScore;
  private long timestamp;

  public Comment(String nickname, String commentContent, long timestamp) {
    this.nickname = nickname;
    this.commentContent = commentContent;
    this.timestamp = timestamp;
  }

  public Comment(String nickname, String commentContent, double sentimentScore, long timestamp) {
    this.nickname = nickname;
    this.commentContent = commentContent;
    this.sentimentScore = sentimentScore;
    this.timestamp = timestamp;
  }

  public String getNickname() {
    return this.nickname;
  }

  public String getCommentContent() {
    return this.commentContent;
  }

  public long getTimestamp() {
    return this.timestamp;
  }
}
