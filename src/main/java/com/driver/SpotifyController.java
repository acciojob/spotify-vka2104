package com.driver;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spotify")
public class SpotifyController {

    //Autowire will not work in this case, no need to change this and add autowire
    SpotifyService spotifyService = new SpotifyService();

    @PostMapping("/add-user")
    public String createUser(@RequestParam(name = "name") String name, String mobile){
        User newUser = spotifyService.createUser(name, mobile);
        if(newUser != null) {
            return "User created Successfully.";
        }
        return "issue while creating User.";
    }

    @PostMapping("/add-artist")
    public String createArtist(@RequestParam(name = "name") String name){
        Artist newArtist = spotifyService.createArtist(name);
        if(newArtist != null) {
            return "Artist created Successfully.";
        }
        return "issue while creating an Artist.";
    }

    @PostMapping("/add-album")
    public String createAlbum(@RequestParam(name = "title") String title, String artistName){
        //If the artist does not exist, first create an artist with given name
        //Create an album with given title and artist
        Album newAlbum = spotifyService.createAlbum(title, artistName);
        if(newAlbum != null) {
            return "Album created Successfully.";
        }
        return "issue while creating an Album.";
    }

    @PostMapping("/add-song")
    public String createSong(String title, String albumName, int length) throws Exception{
        //If the album does not exist in database, throw "Album does not exist" exception
        //Create and add the song to respective album
        try {
            Song newSong = spotifyService.createSong(title, albumName, length);
            if(newSong != null) {
                return "Song created Successfully.";
            } else {
                return "issue while creating a Song.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add-playlist-on-length")
    public String createPlaylistOnLength(String mobile, String title, int length) throws Exception{
        //Create a playlist with given title and add all songs having the given length in the database to
        // that playlist
        //The creater of the playlist will be the given user and will also be the only listener at the
        // time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        try {
            Playlist newPlayList = spotifyService.createPlaylistOnLength(mobile, title, length);
            if(newPlayList != null) {
                return "PlayList created Successfully.";
            } else {
                return "issue while creating a Song.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/add-playlist-on-name")
    public String createPlaylistOnName(@RequestParam String mobile, @RequestParam String title, @RequestParam("songTitles") List<String> songTitles) throws Exception{
        //Create a playlist with given title and add all songs having the given titles in the
        // database to that playlist
        //The creater of the playlist will be the given user and will also be the only listener
        // at the time of playlist creation
        //If the user does not exist, throw "User does not exist" exception
        try {
            Playlist newPlayList = spotifyService.createPlaylistOnName(mobile, title, songTitles);
            if(newPlayList != null) {
                return "PlayList created Successfully.";
            } else {
                return "issue while creating a Song.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/find-playlist")
    public String findPlaylist(String mobile, String playlistTitle) throws Exception{
        //Find the playlist with given title and add user as listener of that playlist and update
        // user accordingly
        //If the user is creater or already a listener, do nothing
        //If the user does not exist, throw "User does not exist" exception
        //If the playlist does not exists, throw "Playlist does not exist" exception
        // Return the playlist after updating
        try {
            Playlist updatedPlayList = spotifyService.findPlaylist(mobile, playlistTitle);
            if(updatedPlayList != null) {
                return "PlayList Updated Successfully.";
            } else {
                return "issue while updating a Play List.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/like-song")
    public String likeSong(String mobile, String songTitle) throws Exception{
        //The user likes the given song. The corresponding artist of the song gets auto-liked
        //A song can be liked by a user only once. If a user tried to like a song multiple times,
        // do nothing
        //However, an artist can indirectly have multiple likes from a user,
        // if the user has liked multiple songs of that artist.
        //If the user does not exist, throw "User does not exist" exception
        //If the song does not exist, throw "Song does not exist" exception
        //Return the song after updating
        try {
            Song updatedSong = spotifyService.likeSong(mobile, songTitle);
            if(updatedSong != null) {
                return "Song and Artist are liked Successfully.";
            } else {
                return "issue while liking a Song.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/popular-artist")
    public String mostPopularArtist(){
        //Return the artist name with maximum likes
        return "";

    }

    @GetMapping("/popular-song")
    public String mostPopularSong(){
        //return the song title with maximum likes
        return "";
    }
}
