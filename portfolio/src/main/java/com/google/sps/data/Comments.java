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

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the template for a list of comments
 * The nickname is added first followed by its corresponding comment
 */
public class Comments {
    /* 
    A comment is composed of two elements (nickname and content); 
    so only 20 comments will be part of the list
    */
    private final int commentLimit = 40;

    // List of comments follow the format nickname then comment content
    private final List<String> commentList = new ArrayList<>();

    private void addNewComment(String nickname, String commentContent) {
        if(commentList.size() == commentLimit) {
            removeOldestComment;
        }
        
        commentList.add(nickname);
        commentList.add(commentContent);
    }

    // Delete the nickname and comment content of the oldest comment
    private void removeOldestComment() {
        commentList.remove(1); // Delete comment content
        commentList.remove(0); // Delete nickname
    }
}