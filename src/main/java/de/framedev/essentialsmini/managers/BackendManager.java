package de.framedev.essentialsmini.managers;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneOptions;
import de.framedev.essentialsmini.main.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BackendManager {
    private final Main plugin;

    public enum DATA {
        NAME("name"),
        MONEY("money"),
        BANKNAME("bankname"),
        BANKMEMBERS("bankmembers"),
        BANKOWNER("bankowner"),
        BANK("bank"),
        KILLS("kills"),
        DAMAGE("damage"),
        ENTITYKILLS("entityKills"),
        DEATHS("deaths"),
        BLOCKSBROKEN("blocksBroken"),
        BLOCKSPLACEN("blocksPlacen"),
        BLOCKBROKEN("blockBroken"),
        BLOCKPLACEN("blockPlacen"),
        COMMANDSUSED("commandsUsed"),
        KEY("key"),
        ENTITYTYPES("entityTypes"),
        OFFLINE("offline"),
        SLEEPTIMES("sleepTimes"),
        CREATEDATE("createDate"),
        LASTLOGIN("lastLogin"),
        LASTLOGOUT("lastLogout");

        private final String name;

        DATA(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public BackendManager(Main plugin) {
        this.plugin = plugin;
    }

    public void createUserMoney(OfflinePlayer player, String collection) {
        String uuid = player.getUniqueId().toString();
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document result = collections.find(new Document("uuid", uuid)).first();
            if (result == null) {
                Document dc = (new Document("uuid", uuid))
                        .append("name", player.getName())
                        .append("money", 0.0)
                        .append("bankname", "")
                        .append("bankmembers", new ArrayList<String>())
                        .append("bankowner", "")
                        .append("bank", 0.0)
                        .append("kills", 0)
                        .append("damage", 0.0)
                        .append("entityKills", 0)
                        .append("deaths", 0)
                        .append("blocksBroken", new ArrayList<String>())
                        .append("blocksPlacen", new ArrayList<String>())
                        .append("blockBroken", 0)
                        .append("blockPlacen", 0)
                        .append("commandsUsed", 0)
                        .append("key", null)
                        .append("entityTypes", new ArrayList<String>())
                        .append("offline", false)
                        .append(DATA.SLEEPTIMES.getName(), 0)
                        .append("createDate", System.currentTimeMillis() + "")
                        .append("lastLogin", 0L + "")
                        .append("lastLogout", 0L + "");
                collections.insertOne(dc, (new InsertOneOptions()).bypassDocumentValidation(false));
            }
        } else {
            this.plugin.getMongoManager().getDatabase().createCollection(collection);
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document result = collections.find(new Document("uuid", uuid)).first();
            if (result == null) {
                Document dc = (new Document("uuid", uuid))
                        .append("name", player.getName())
                        .append("money", 0)
                        .append("bankname", "")
                        .append("bankmembers", new ArrayList<String>())
                        .append("bankowner", "")
                        .append("bank", 0.0)
                        .append("kills", 0)
                        .append("damage", 0.0)
                        .append("entityKills", 0)
                        .append("deaths", 0)
                        .append("blocksBroken", new ArrayList<String>())
                        .append("blocksPlacen", new ArrayList<String>())
                        .append("blockBroken", 0)
                        .append("blockPlacen", 0)
                        .append("commandsUsed", 0)
                        .append("key", null)
                        .append("entityTypes", new ArrayList<String>())
                        .append("offline", false)
                        .append(DATA.SLEEPTIMES.getName(), 0)
                        .append("createDate", System.currentTimeMillis())
                        .append("lastLogin", 0L)
                        .append("lastLogout", 0L);
                collections.insertOne(dc, (new InsertOneOptions()).bypassDocumentValidation(false));
            }
        }
    }

    /**
     * @param where from the Database Document
     * @param data Data in where
     * @param selected the Selected key in your Database
     * @param collection the Collection in your Database
     * @return data from Database
     */
    public Object getObject(String where, Object data, String selected, String collection) {
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document(where, data)).first();
            if (document != null) {
                return document.get(selected);
            }
        }
        return null;
    }

    public void updataData(String where, Object data, String selected, Object dataSelected, String collection) {
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document(where, data)).first();
            if (document != null) {
                Document document1 = new Document(selected, dataSelected);
                Document document2 = new Document("$set", document1);
                if (document.get(where) != null) {
                    collections.updateOne(document, document2);
                } else {
                    document.put(selected, dataSelected);
                    collections.updateOne(collections.find(new Document(where, data)).first(), document);
                }
            }
        } else {
            this.plugin.getMongoManager().getDatabase().createCollection(collection);
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document(where, data)).first();
            if (document != null) {
                Document document1 = new Document(selected, dataSelected);
                Document document2 = new Document("$set", document1);
                collections.updateOne(document, document2);
            }
        }
    }

    public void updateUser(OfflinePlayer player, String where, Object data, String collection) {
        if (existsCollection(collection)) {
            String uuid = player.getUniqueId().toString();
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document("uuid", uuid)).first();
            if (document != null) {
                Document document1 = new Document(where, data);
                Document document2 = new Document("$set", document1);
                if (document.get(where) != null) {
                    collections.updateOne(document, document2);
                } else {
                    document.put(where, data);
                    collections.updateOne(collections.find(new Document("uuid", uuid)).first(), document);
                }
            }
        } else {
            String uuid = player.getUniqueId().toString();
            this.plugin.getMongoManager().getDatabase().createCollection(collection);
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document("uuid", uuid)).first();
            if (document != null) {
                Document document1 = new Document(where, data);
                Document document2 = new Document("$set", document1);
                collections.updateOne(document, document2);
            }
        }
    }

    public boolean exists(String where, Object data, String whereSelected, String collection) {
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document(where, data)).first();
            if (document != null) {
                return document.get(whereSelected) != null;
            }
        }
        return false;
    }

    public boolean exists(OfflinePlayer player, String where, String collection) {
        if (existsCollection(collection)) {
            String uuid = player.getUniqueId().toString();
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document("uuid", uuid)).first();
            if (document != null) {
                return document.get(where) != null;
            }
        }
        return false;
    }

    public void insertData(OfflinePlayer player, String where, Object data, String collection) {
        if (existsCollection(collection)) {
            String uuid = player.getUniqueId().toString();
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document("uuid", uuid)).first();
            if (document != null) {
                collections.updateOne(new Document("uuid", uuid),
                        new Document("$set", new Document(where, data)));
            }
        }
    }

    public void insertData(String where, Object data, String newKey, Object newValue, String collection) {
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document(where, data)).first();
            if (document != null) {
                collections.updateOne(new Document(where, data),
                        new Document("$set", new Document(newKey, newValue)));
            }
        }
    }

    public void deleteUser(OfflinePlayer player, String collection) {
        if (existsCollection(collection)) {
            String uuid = player.getUniqueId().toString();
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document("uuid", uuid)).first();
            if (document != null) {
                collections.deleteOne(document);
            }
        } else {
            String uuid = player.getUniqueId().toString();
            this.plugin.getMongoManager().getDatabase().createCollection(collection);
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            Document document = collections.find(new Document("uuid", uuid)).first();
            if (document != null) {
                collections.deleteOne(document);
            }
        }
    }

    public Object get(OfflinePlayer player, String where, String collection) {
        if (existsCollection(collection)) {
            MongoCollection<Document> mongoCollection = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            String str = player.getUniqueId().toString();
            Document document1 = mongoCollection.find(new Document("uuid", str)).first();
            if (document1 != null) {
                return document1.get(where);
            }
            return null;
        }
        this.plugin.getMongoManager().getDatabase().createCollection(collection);
        MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
        String uuid = player.getUniqueId().toString();
        collections.insertOne(new Document());
        Document document = collections.find(new Document("uuid", uuid)).first();
        if (document != null) {
            return document.get(where);
        }
        return null;
    }


    public boolean existsCollection(String collection) {
        MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
        return collections != null;
    }


    @SuppressWarnings("deprecation")
    public ArrayList<OfflinePlayer> getOfflinePlayers(String collection) {
        ArrayList<OfflinePlayer> players = new ArrayList<>();
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            collections.find(new Document("offline", true)).forEach((Block<? super Document>) document -> {
                if (document != null) {
                    UUID uuid = UUID.fromString(document.getString("uuid"));
                    players.add(Bukkit.getOfflinePlayer(uuid));
                }
            });
            return players;
        }
        return null;
    }

    public List<Object> getList(String where, Object data, String selected, String collection) {
        ArrayList<Object> list = new ArrayList<>();
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            collections.find(new Document(where, data)).forEach((Consumer<? super Document>) document -> {
                if (document != null) {
                    list.add(document.get(selected));
                }
            });
            return list;
        }
        return null;
    }

    public List<Document> getAllDocuments(String collection) {
        List<Document> list = new ArrayList<>();
        if (existsCollection(collection)) {
            MongoCollection<Document> collections = this.plugin.getMongoManager().getDatabase().getCollection(collection);
            FindIterable<Document> find = collections.find();
            for (Document document : find) {
                list.add(document);
            }
        }
        return list;
    }
}

