package vttp2023.batch3.csf.assessment.cnserver;

import org.bson.Document;

import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;

public class Utility {

    public static TagCount toTagCount(Document doc){
        TagCount tagCount = new TagCount(doc.getString("_id"),
            doc.getInteger("count"));
        return tagCount;
    }

    public static News toNews(Document doc){
        News news = new News();
        news.setId(doc.getObjectId("_id").toString());
        news.setPostDate(doc.getLong("postDate"));
        news.setTitle(doc.getString("title"));
        news.setImage(doc.getString("image"));
        news.setTags(doc.getList("tags", String.class));
        return news;
    } 
    
}
