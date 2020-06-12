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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. * */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.ASCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> commentsList = new ArrayList<>();
    for (Entity entity : results.asList(FetchOptions.Builder.withDefaults())) {
      String nickname = (String) entity.getProperty("nickname");
      String commentContent = (String) entity.getProperty("commentContent");
      double sentimentScore = (double) entity.getProperty("sentimentScore");

      Comment comment = new Comment(nickname, commentContent, sentimentScore);
      commentsList.add(comment);
      Comment why = new Comment("omg", "omg", 0.8);
      commentsList.add(why);
    }

    String json = toJsonString(commentsList);
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Comment comment = getComment(request);

    Document doc =
        Document.newBuilder()
            .setContent(comment.getCommentContent())
            .setType(Document.Type.PLAIN_TEXT)
            .build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    double score = sentiment.getScore();
    languageService.close();


    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("nickname", comment.getNickname());
    commentEntity.setProperty("commentContent", comment.getCommentContent());
    commentEntity.setProperty("sentimentScore", score);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

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
