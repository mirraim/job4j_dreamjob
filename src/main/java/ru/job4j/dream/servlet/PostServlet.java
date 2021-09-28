package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class PostServlet extends HttpServlet {
    private final Store store = PsqlStore.instOf();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        store.save(
                new Post(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("posts", new ArrayList<>(store.findAllPosts()));
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("posts.jsp").forward(req, resp);
    }
}
