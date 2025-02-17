package de.interaapps.pastefy.controller;

import de.interaapps.pastefy.exceptions.NotFoundException;
import de.interaapps.pastefy.model.database.Folder;
import de.interaapps.pastefy.model.database.User;
import de.interaapps.pastefy.model.requests.CreateFolderRequest;
import de.interaapps.pastefy.model.responses.ActionResponse;
import de.interaapps.pastefy.model.responses.folder.CreateFolderResponse;
import de.interaapps.pastefy.model.responses.folder.FolderResponse;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.With;
import org.javawebstack.httpserver.router.annotation.params.Attrib;
import org.javawebstack.httpserver.router.annotation.params.Body;
import org.javawebstack.httpserver.router.annotation.params.Path;
import org.javawebstack.httpserver.router.annotation.verbs.Delete;
import org.javawebstack.httpserver.router.annotation.verbs.Get;
import org.javawebstack.httpserver.router.annotation.verbs.Post;
import org.javawebstack.orm.Repo;

@PathPrefix("/api/v2/folder")
public class FolderController extends HttpController {
    @Post
    @With("auth")
    public CreateFolderResponse createFolder(Exchange exchange, @Body CreateFolderRequest request, @Attrib("user") User user) {
        CreateFolderResponse response = new CreateFolderResponse();

        Folder folder = new Folder();
        folder.setUserId(user.getId());
        folder.setName(request.name);

        Folder parent = Repo.get(Folder.class).where("key", request.parent).first();

        if (parent != null && parent.getUserId().equals(user.getId()))
            folder.setParent(parent);

        folder.save();
        response.success = true;
        response.folder = new FolderResponse(folder);

        return response;
    }

    @Get("/{id}")
    public FolderResponse getFolder(Exchange exchange, @Path("id") String id, @Attrib("user") User user) {
        Folder folder = Repo.get(Folder.class).where("key", id).first();
        if (folder == null)
            throw new NotFoundException();
        return new FolderResponse(folder, true, exchange.rawRequest().getParameter("hide_children") == null);
    }

    @Delete("/{id}")
    @With("auth")
    public ActionResponse delete(Exchange exchange, @Path("id") String id, @Attrib("user") User user) {
        ActionResponse response = new ActionResponse();
        Folder folder = Repo.get(Folder.class).where("key", id).first();

        if (folder != null) {
            if (folder.getUserId().equals(user.getId())) {
                folder.delete();
                response.success = true;
            }
        }

        return response;
    }
}
