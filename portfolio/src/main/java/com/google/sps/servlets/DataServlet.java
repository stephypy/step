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

<<<<<<< HEAD
=======
import com.google.gson.Gson;
import com.google.sps.data.Comments;
>>>>>>> 46ec868... Added functionality to comment section
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. **/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
<<<<<<< HEAD

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println("<h1>Hello world!</h1>");
  }
=======
    private Comments commentsList = new Comments();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = toJsonString(commentsList);
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nicknameChoice = "Nickname:" + getNickname(request);
        String commentContent = "Comment:" + getComment(request);

        commentsList.addNewComment(nicknameChoice, commentContent);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
    }

    // TODO: @stephypy Handle empty text fields
    private String getNickname(HttpServletRequest request) {
        return request.getParameter("nickname");
    }

    private String getComment(HttpServletRequest request) {
        return request.getParameter("comment");
    }

    private static String toJsonString(Comments data) {
        return new Gson().toJson(data);
    }
>>>>>>> 46ec868... Added functionality to comment section
}
