/**
 * 
 */
package tech.ozak.sportmix.handler;

import android.text.Html;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.ozak.sportmix.dto.RssItem;
import tech.ozak.sportmix.romerss.RomeRssHandler;

/**
 * @author rob
 *
 */
public class RssReader {
	
	private final static String BOLD_OPEN = "<B>";
	private final static String BOLD_CLOSE = "</B>";
	private final static String BREAK = "<BR>";
	private final static String ITALIC_OPEN = "<I>";
	private final static String ITALIC_CLOSE = "</I>";
	private final static String SMALL_OPEN = "<SMALL>";
	private final static String SMALL_CLOSE = "</SMALL>";

	/**
	 * This method defines a feed URL and then calles our SAX Handler to read the article list
	 * from the stream
	 * 
	 * @return List<JSONObject> - suitable for the List View activity
	 */
	public static List<RssItem> getLatestRssFeed(String feed){
		// feed = "http://www.ntvspor.net/Rss/anasayfa";
		
		
		RomeRssHandler rss=new RomeRssHandler();

		List<RssItem> articles = null;
		try {
			if (StringUtils.containsIgnoreCase(feed,"trtspor")){
				RSSHandler rssHandler=new RSSHandler();
				articles=rssHandler.getLatestArticles(feed);
			}
			else{
				articles = rss.getAllNews(feed);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//return fillData(articles);
        return articles;
	}
	
	
	/**
	 * This method takes a list of Article objects and converts them in to the 
	 * correct JSON format so the info can be processed by our list view
	 * 
	 * @param articles - list<Article>
	 * @return List<JSONObject> - suitable for the List View activity
	 */
	private static List<JSONObject> fillData(List<RssItem> articles) {

        List<JSONObject> items = new ArrayList<JSONObject>();
        for (RssItem article : articles) {
            JSONObject current = new JSONObject();
            try {
            	buildJsonObject(article, current);
			} catch (JSONException e) {
				Log.e("RSS ERROR", "Error creating JSON Object from RSS feed");
			}
			items.add(current);
        }
        
        return items;
	}


	/**
	 * This method takes a single Article Object and converts it in to a single JSON object
	 * including some additional HTML formating so they can be displayed nicely
	 * 
	 * @param article
	 * @param current
	 * @throws org.json.JSONException
	 */
	private static void buildJsonObject(RssItem article, JSONObject current) throws JSONException {
		String title = article.getTitle();
		String description = article.getDescription();
		String date = article.getPubDate();
		String imgLink = article.getImgLink();
		
		StringBuffer sb = new StringBuffer();
		sb.append(BOLD_OPEN).append(title).append(BOLD_CLOSE);
		sb.append(BREAK);
		sb.append(description);
		sb.append(BREAK);
		sb.append(SMALL_OPEN).append(ITALIC_OPEN).append(date).append(ITALIC_CLOSE).append(SMALL_CLOSE);
		
		current.put("text", Html.fromHtml(sb.toString()));
		current.put("imageLink", imgLink);
	}
}
