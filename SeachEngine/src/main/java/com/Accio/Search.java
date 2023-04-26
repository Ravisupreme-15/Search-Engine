package com.Accio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")

public class Search extends HttpServlet {

    protected void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException {



        // getting the keyword by from the frontend/ user
        String keyword = request.getParameter("keyword");

        // setting the connection to database

        Connection connection = DatabaseConnection.getConnection();


        try{


            // store the query of user
          PreparedStatement preparedStatement = connection.prepareStatement("Insert Into History Values(?,?);");

          preparedStatement.setString(1,keyword);
          preparedStatement.setString(2, "http://localhost:8080/SeachEngine/Search?keyword="+keyword);
          preparedStatement.executeUpdate();

            // getting results after running the  ranking query
            // transfering the values from result set to restult arraylists

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT page_title, page_link,(length(lower(page_text)) - length(replace(lower(page_text),'" + keyword.toLowerCase() + "','')))/length('" + keyword.toLowerCase() + "')\n" +
                    " as countoccurance from pages order by countoccurance desc limit 30;");

            ArrayList<SearchResults> results = new ArrayList<>();
            while (resultSet.next()) {

                SearchResults searchResults = new SearchResults();
                searchResults.setTitle(resultSet.getString("page_title"));
                searchResults.setLink(resultSet.getString("page_link"));
                results.add(searchResults);
            }
            // displaying res arraylist in console;
//            for(SearchResults result : results){
//                System.out.println(result.getTitle()+"\n"+result.getLink()+"\n");
//            }

            request.setAttribute("results",results);
            request.getRequestDispatcher("search.jsp").forward(request,response);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
        }
        catch (SQLException | ServletException sqlException){
            sqlException.printStackTrace();
        }


    }
}
