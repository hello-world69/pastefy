package de.interaapps.pastefy.model.requests.paste;

import de.interaapps.pastefy.model.database.Paste;
import org.javawebstack.validator.Rule;

public class CreatePasteRequest {
    @Rule("string")
    public String title = "";
    @Rule({"required", "string"})
    public String content;
    @Rule("boolean")
    public boolean encrypted = false;
    public String folder;

    public Paste.Type type = Paste.Type.PASTE;
}
