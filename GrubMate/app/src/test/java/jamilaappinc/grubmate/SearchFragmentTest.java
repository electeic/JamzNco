package jamilaappinc.grubmate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Terence Zhang on 10/30/2017.
 */
public class SearchFragmentTest {
    @Test
    public void testSearchNameOnly() throws Exception {
        ArrayList<Post> myList = new ArrayList<Post>();
        Post p = new Post();
        p.setmTitle("Honey Cake");
        myList.add(p);
        Post w = new Post();
        w.setmTitle("BBQ");
        myList.add(w);
        Post x = new Post();
        x.setmTitle("Pork");
        myList.add(x);
        ArrayList<Post> filteredPosts = SearchFragment.filterPosts(myList,"BBQ",null,null,new ArrayList<String>());

        assertEquals(filteredPosts.size(),1);
        assertEquals(filteredPosts.get(0).mTitle,"BBQ");
    }

    @Test
    public void testSearchStartTimeOnly() throws Exception {
        ArrayList<Post> myList = new ArrayList<Post>();
        Post p = new Post();
        p.setmTitle("Honey Cake");
        p.setmStartDate(new Date(2017,10,30,10,8));
        myList.add(p);
        Post w = new Post();
        w.setmStartDate(new Date(2017,10,29,10,8));

        w.setmTitle("BBQ");
        myList.add(w);
        Post x = new Post();
        x.setmStartDate(new Date(2017,10,28,10,8));

        x.setmTitle("Pork");
        myList.add(x);
        ArrayList<Post> filteredPosts = SearchFragment.filterPosts(myList,"",new Date(2017,10,29,9,8),null,new ArrayList<String>());


        assertEquals(filteredPosts.size(),2);
        assertEquals(filteredPosts.get(0).mTitle,"Honey Cake");
        assertEquals(filteredPosts.get(1).mTitle,"BBQ");

    }

    @Test
    public void testSearchEndTimeOnly() throws Exception {
        ArrayList<Post> myList = new ArrayList<Post>();
        Post p = new Post();
        p.setmTitle("Honey Cake");
        p.setmStartDate(new Date(2017,10,30,10,8));
        p.setmEndDate(new Date(2017,11,7,10,8));

        myList.add(p);
        Post w = new Post();
        w.setmStartDate(new Date(2017,10,29,10,8));
        w.setmEndDate(new Date(2017,11,3,10,8));

        w.setmTitle("BBQ");
        myList.add(w);
        Post x = new Post();
        x.setmStartDate(new Date(2017,10,28,10,8));
        x.setmEndDate(new Date(2017,11,5,10,8));

        x.setmTitle("Pork");
        myList.add(x);
        ArrayList<Post> filteredPosts = SearchFragment.filterPosts(myList,"",null,new Date(2017,11,4,10,8),new ArrayList<String>());


        assertEquals(filteredPosts.size(),1);
        assertEquals(filteredPosts.get(0).mTitle,"BBQ");

    }
    @Test
    public void testSearchTagsOnly() throws Exception {
        ArrayList<Post> myList = new ArrayList<Post>();
        Post p = new Post();
        p.setmTitle("Honey Cake");
        p.setmStartDate(new Date(2017,10,30,10,8));
        p.setmEndDate(new Date(2017,11,7,10,8));
        ArrayList<String> tags = new ArrayList<>();
        tags.add("1");
        tags.add("2");
        tags.add("3");
        p.setmTags(tags);
        myList.add(p);

        Post w = new Post();
        w.setmStartDate(new Date(2017,10,29,10,8));
        w.setmEndDate(new Date(2017,11,3,10,8));

        w.setmTitle("BBQ");
        ArrayList<String> wTags = new ArrayList<>();
        wTags.add("4");
        wTags.add("5");
        wTags.add("6");
        w.setmTags(wTags);

        myList.add(w);
        Post x = new Post();
        x.setmStartDate(new Date(2017,10,28,10,8));
        x.setmEndDate(new Date(2017,11,5,10,8));

        x.setmTitle("Pork");
        ArrayList<String> xTags = new ArrayList<>();
        xTags.add("7");
        xTags.add("8");
        xTags.add("9");
        x.setmTags(xTags);
        myList.add(x);

        ArrayList<Post> filteredPosts = SearchFragment.filterPosts(myList,"",null,null,xTags);


        assertEquals(filteredPosts.size(),1);
        assertEquals(filteredPosts.get(0).mTitle,"Pork");
    }

    @Test
    public void testSearchAll() throws Exception {
        ArrayList<Post> myList = new ArrayList<Post>();
        Post p = new Post();
        p.setmTitle("Honey Cake");
        p.setmStartDate(new Date(2017,10,30,10,8));
        p.setmEndDate(new Date(2017,11,7,10,8));
        ArrayList<String> tags = new ArrayList<>();
        tags.add("1");
        tags.add("2");
        tags.add("3");
        p.setmTags(tags);
        myList.add(p);

        Post w = new Post();
        w.setmStartDate(new Date(2017,10,29,10,8));
        w.setmEndDate(new Date(2017,11,3,10,8));

        w.setmTitle("BBQ");
        ArrayList<String> wTags = new ArrayList<>();
        wTags.add("4");
        wTags.add("5");
        wTags.add("6");
        w.setmTags(wTags);

        myList.add(w);

        Post y = new Post();
        y.setmStartDate(new Date(2017,10,29,10,8));
        y.setmEndDate(new Date(2017,11,6,10,8));

        y.setmTitle("BBQ");
        ArrayList<String> yTags = new ArrayList<>();
        yTags.add("10");
        yTags.add("11");
        yTags.add("12");
        y.setmTags(yTags);

        myList.add(y);



        Post x = new Post();
        x.setmStartDate(new Date(2017,10,28,10,8));
        x.setmEndDate(new Date(2017,11,5,10,8));

        x.setmTitle("Pork");
        ArrayList<String> xTags = new ArrayList<>();
        xTags.add("7");
        xTags.add("8");
        xTags.add("9");
        x.setmTags(xTags);
        myList.add(x);

        Post z = new Post();
        z.setmStartDate(new Date(2017,10,28,10,8));
        z.setmEndDate(new Date(2017,11,5,10,8));

        z.setmTitle("Pork");
        ArrayList<String> zTags = new ArrayList<>();
        zTags.add("15");
        zTags.add("16");
        zTags.add("17");
        z.setmTags(zTags);
        myList.add(z);

        ArrayList<Post> filteredPosts = SearchFragment.filterPosts(myList,"BBQ",new Date(2017,10,28,9,8),new Date(2017,11,7,10,8),yTags);


        assertEquals(filteredPosts.size(),1);
        assertEquals(filteredPosts.get(0).mTitle,"BBQ");
    }
}