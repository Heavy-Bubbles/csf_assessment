package vttp2023.batch3.csf.assessment.cnserver.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.csf.assessment.cnserver.models.News;

@Repository
public class NewsRepository {

	@Autowired private MongoTemplate template;

	// TODO: Task 1 
	// Write the native Mongo query in the comment above the method
	/* 
	db.test.inssert({
		postDate: 4390583456,
		title: "title",
		description: "description",
		image: "url",
		tags: ["tag1", "tag2", "tag3"]
	})
	*/
	public String insertPost(News news){
		Document doc = new Document();
		doc.put("postDate", news.getPostDate());
		doc.put("title", news.getTitle());
		doc.put("description", news.getDescription());
		doc.put("image", news.getImage());
		if(news.getTags().size() > 0){
			doc.put("tags", news.getTags());
		}
		Document newDoc = template.insert(doc, "test");
		return newDoc.getObjectId("_id").toString();
	}

	

	// TODO: Task 2 
	// Write the native Mongo query in the comment above the method
	/*
	db.test.aggregate([
		{
			$match: {
				postDate: { $gte: 4200000000 }
			}
		},
		{
			$project: { tags: 1 }  
		},
		{
			$unwind: "$tags"
		},
		{
			$group: {
				_id: "$tags",
				count: { $sum: 1 }
			}
		},
		{
			$sort: { count: -1, _id: 1 }
		},
		{
			$limit: 10
		}
	])
	*/
	public List<Document> getTags(Integer duration){

		Long currentTime = System.currentTimeMillis();
		Long convert = Long.valueOf(duration * 60000);
		Long queryTime = currentTime - convert;

        MatchOperation matchTime = Aggregation.match(
            Criteria.where("postDate").gte(queryTime)
        );

        ProjectionOperation projectFields = Aggregation.project("tags");

		UnwindOperation unwind = Aggregation.unwind("tags");

		GroupOperation groupByTags = Aggregation.group("tags")
			.count().as("count");

		SortOperation sort = Aggregation.sort(Sort.by(Direction.DESC, "count"))
			.and(Direction.ASC, "_id");

		LimitOperation limit = Aggregation.limit(10);

        Aggregation pipeline = Aggregation.newAggregation(matchTime, projectFields, unwind, groupByTags, sort, limit);

        return template.aggregate(pipeline, "test", Document.class).getMappedResults();

	}
	// TODO: Task 3
	// Write the native Mongo query in the comment above the method
	/*
	 db.test.find({
			tags:{
				$in: [ "tag" ]
			}
		})
	 */
	public List<Document> getNewsByTag(String tag){
        Criteria criteria = Criteria.where("tags")
            .in(tag);

        Query query = Query.query(criteria);

        return template.find(query, Document.class, "test");
    }

}
