package vttp2023.batch3.csf.assessment.cnserver.controllers;

import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.repositories.ImageRepository;
import vttp2023.batch3.csf.assessment.cnserver.services.NewsService;

@RestController
@RequestMapping(path = "/api")
public class NewsController {

	@Autowired
	private ImageRepository imageRepo;

	@Autowired
	private NewsService newsService;

	// TODO: Task 1
	@PostMapping(path = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> postNews(@RequestPart String title, @RequestPart MultipartFile photo,
		@RequestPart String description, @RequestPart String tags){

			try{
				String contentType = photo.getContentType();
				InputStream is = photo.getInputStream();
				String url = imageRepo.uploadPhoto(contentType, is);
				System.out.println(url);

				List<String> tagList = new LinkedList<>();
				JsonReader reader = Json.createReader(new StringReader(tags));
				JsonArray array = reader.readArray();
				if (array != null){
					for (JsonValue tag: array){   
					tagList.add(array.getString(array.indexOf(tag)));  
					}
				}
			
				News news = new News();
				news.setPostDate(System.currentTimeMillis());
				news.setTitle(title);
				news.setDescription(description);
				news.setImage(url);
				news.setTags(tagList);
				String id = newsService.postNews(news);
				System.out.println(id);
				JsonObject resp = Json.createObjectBuilder()
					.add("id", id)
					.build();

				return ResponseEntity.ok(resp.toString());

			} catch (Exception ex){
				JsonObject resp = Json.createObjectBuilder()
					.add("message", ex.getMessage())
					.build();
            	return ResponseEntity.status(500)
                	.body(resp.toString());
			}

		}


	// TODO: Task 2
	@GetMapping(path = "/tags")
	@ResponseBody
	public ResponseEntity<List<TagCount>> getTags(@RequestParam Integer duration){
		List<TagCount> result = newsService.getTags(duration);
		return ResponseEntity.ok(result);
	}


	// TODO: Task 3
	@GetMapping(path = "/news")
	@ResponseBody
	public ResponseEntity<List<News>> getNewsByTag(@RequestParam String tag){
		List<News> result = newsService.getNewsByTag(tag);
		return ResponseEntity.ok(result);
	}

}
