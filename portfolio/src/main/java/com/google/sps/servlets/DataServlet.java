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

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.util.List; 
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. **/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    private final int commentLimit = 5;
    private List<Comment> commentsList = new ArrayList<>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = toJsonString(commentsList);
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Remove oldest comment
        if(commentsList.size() == commentLimit) {
            commentsList.remove(0);
        }

        Comment comment = getComment(request);
        commentsList.add(comment);

        Entity taskEntity = new Entity("Task");
        taskEntity.setProperty("nickname", comment.getNickname());
        taskEntity.setProperty("commentContent", comment.getCommentContent());

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(taskEntity);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
    }

    private Comment getComment(HttpServletRequest request) {
        String nickname = request.getParameter("nickname");
        String commentContent = request.getParameter("comment");
        return new Comment(nickname, commentContent);
    }

    private static String toJsonString(List<Comment> data) {
        return new Gson().toJson(data);
    }
}
