package vietnamworks.com.jobapp.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import R.helper.LocalStorage;
import vietnamworks.com.jobapp.entities.SearchHistoryEntity;

/**
 * Created by duynk on 2/23/16.
 *
 */
public class SearchHistoryModel {
    public final static String LS_KEY = "search_history";
    public final static int STACK_SIZE = 10;

    static ArrayList<SearchHistoryEntity> data;
    public static void load() {
        if (data == null) {
            //LocalStorage.remove(LS_KEY);
            data = new ArrayList<>();
            try {
                JSONObject obj = LocalStorage.getJSON(LS_KEY);
                if (obj != null){
                    JSONArray array = obj.getJSONArray("data");
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ele = array.getJSONObject(i);
                            SearchHistoryEntity entity = new SearchHistoryEntity();
                            entity.importFromJson(ele);
                            data.add(entity);
                        }
                    }
                }
            }catch (Exception E) {
                E.printStackTrace();
            }
        }
    }

    public static void add(SearchHistoryEntity ele) {
        for (SearchHistoryEntity e: data) {
            if (ele.equals(e)) {
                data.remove(e);
                break;
            }
        }
        if (data.size() > STACK_SIZE) {
            data.remove(data.size()-1);
        }
        data.add(0, ele);
    }

    public static int size() {
        return data.size();
    }

    public static SearchHistoryEntity get(int index) {
        return data.get(index);
    }

    public static void save() {
        if (data != null) {
            try {
                JSONObject jsonObj = new JSONObject();
                JSONArray arr = new JSONArray();
                for (SearchHistoryEntity entity : data) {
                    JSONObject ele = entity.exportToJson();
                    arr.put(ele);
                }
                jsonObj.put("data", arr);
                LocalStorage.set(LS_KEY, jsonObj);
            }catch (Exception E) {
                E.printStackTrace();
            }
        }
    }


}
