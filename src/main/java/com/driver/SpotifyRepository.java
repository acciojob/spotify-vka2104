package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User newUser = new User(name, mobile);
        boolean isUserCreated = users.add(newUser);
        if(isUserCreated) return newUser;
        return null;
    }

    public Artist createArtist(String name) {
        Artist newArtist = new Artist(name);
        boolean isArtistAdded = artists.add(newArtist);
        if(isArtistAdded) return newArtist;
        return null;
    }

    public Artist getArtistByName(String name) {
        for(Artist artist: artists) {
            if(artist.getName().equals(name)) {
                return artist;
            }
        }
        return null;
    }
    public Album addToAlbums(String title) {
        Album newAlbum = new Album(title);
        boolean isAblubAdded = albums.add(newAlbum);
        if(isAblubAdded) return newAlbum;
        else return null;
    }
    public ArrayList<Album> addAlbumToAlbumListArray(Album newAlbum) {
        ArrayList<Album> albumList = new ArrayList<>();
        albumList.add(newAlbum);
        return albumList;
    }
    public List<Album> getAlbumsArray(Artist artist) {
        return artistAlbumMap.get(artist);
    }
    public Album addToArtistAlbumMap(String albumTitle, Artist artist) {
        Album newAlbum = addToAlbums(albumTitle);
        if(newAlbum != null) {
            List<Album> AlbumsArray = getAlbumsArray(artist);
            if(AlbumsArray != null) {
                AlbumsArray.add(newAlbum);
                artistAlbumMap.put(artist, AlbumsArray);
            } else {
                artistAlbumMap.put(artist, addAlbumToAlbumListArray(newAlbum));
            }
            return newAlbum;
        }
        return null;
    }
    public Album createAlbum(String title, String artistName) {
        Artist artist = getArtistByName(artistName);
        if(artist != null) {
           return addToArtistAlbumMap(title, artist);
        } else {
            Artist newArtist = createArtist(artistName);
            return addToArtistAlbumMap(title, newArtist);
        }
    }

    public Album getAlbumByName(String albumName) {
        for(Album album: albums) {
            if(album.getTitle().equals(albumName)) return album;
        }
        return null;
    }

    public Song getSongByName(String title) {
        for(Song song: songs) {
            if(song.getTitle().equals(title)) return song;
        }
        return null;
    }

    public Song addToSongs(String title, int length) throws Exception{
        if(getSongByName(title) != null) throw new Exception("Song already exist in the album.");
        Song newSong = new Song(title, length);
        boolean isSongAdded = songs.add(newSong);
        if(isSongAdded) return newSong;
        else return null;
    }
    public List<Song> addSongToSongsListArray(Song newSong) {
        ArrayList<Song> songsList = new ArrayList<>();
        songsList.add(newSong);
        return songsList;
    }
    public List<Song> getSongs(Album album) {
        return albumSongMap.get(album);
    }
    public Song addToAlbumSongMap(String songTitle, int songLength, Album album) throws Exception {
        Song newSong = addToSongs(songTitle, songLength);
        if(newSong != null) {
            List<Song> existingSongs = getSongs(album);
            if(existingSongs != null) {
                existingSongs.add(newSong);
                albumSongMap.put(album, existingSongs);
            } else {
                albumSongMap.put(album, addSongToSongsListArray(newSong));
            }
            return newSong;
        }
        return null;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album = getAlbumByName(albumName);
        if(album != null) {
            return addToAlbumSongMap(title, length, album);
        } else {
            throw new Exception("Album not found.");
        }
    }

    // returns user data based on user mobile number
    public User getUserByMobile(String mobile) {
        for(User user: users) {
            if(user.getMobile().equals(mobile)) return user;
        }
        return null;
    }

    //return the given user and list of playlist against that user
    public Map.Entry<User, List<Playlist>> getUserAndPlayListByMobile(String mobile) {
        for(Map.Entry<User, List<Playlist>> userPlayList: userPlaylistMap.entrySet()) {
            if(userPlayList.getKey().getMobile().equals(mobile)) {
                return userPlayList;
            }
        }
        return null;
    }

    //gets the list of playlist from user the "userPlaylistMap" based on the user mobile number.
    //returns the specific playlist from the userplaylist(List<PlayList>) based on the playlist title
    public Playlist getPlayListByName(String mobile, String title) {
        Map.Entry<User, List<Playlist>> userPlayList = getUserAndPlayListByMobile(mobile);
        if(userPlayList != null) {
            for(Playlist playList: userPlayList.getValue()) {
                if(playList.getTitle().equals(title)) {
                    return playList;
                }
            }
        }
        return null;
    }

     //if the playlist exist against given user. it throws playlist exist exception.
     // else
     // creates a new playlist and returns it.
    public Playlist createNewPlayList(String mobile, String title) throws Exception{
        if(getPlayListByName(mobile, title) != null) throw new Exception("Play List already Exist for this user.");
        Playlist newPlayList = new Playlist(title);
        boolean isPlayListAdded = playlists.add(newPlayList);
        if(isPlayListAdded) return newPlayList;
        else return null;
    }

    // add currentplaylist to arraylist( to create new)
    public List<Playlist> addPlayListToPlayListArray(Playlist newPlayList) {
        List<Playlist> playListArray = new ArrayList<>();
        playListArray.add(newPlayList);
        return playListArray;
    }

    //returns current users existing playlist list
    public List<Playlist> getPlaylistsArray(User user) {
        return userPlaylistMap.get(user);
    }

    //add current user to arraylist( to create new)
    public List<User> addUserToUsersArray(User newUser) {
        List<User> usersArray = new ArrayList<>();
        usersArray.add(newUser);
        return usersArray;
    }

    // returns the list of songs based on song length
    public List<Song> getSongsByLength(int length) {
        List<Song> songsArray = new ArrayList<>();
        for(Song song: songs) {
            if(song.getLength() == length) {
                songsArray.add(song);
            }
        }
        return songsArray;
    }
    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user = getUserByMobile(mobile);
        if(user != null) {
            Playlist newPlayList = createNewPlayList(mobile, title);
            if(newPlayList != null) {
                creatorPlaylistMap.put(user, newPlayList); // maps user and playlist
                List<Playlist> playListArray = getPlaylistsArray(user);
                if(playListArray != null) {
                    playListArray.add(newPlayList);
                    userPlaylistMap.put(user, playListArray); // add this play list to the users playlist list(appending with existing playlist)
                } else {
                    userPlaylistMap.put(user, addPlayListToPlayListArray(newPlayList)); // // add this play list to the users playlist list(adding)
                }
                playlistListenerMap.put(newPlayList, addUserToUsersArray(user)); // mark the creater as a listner of this playlist
                List<Song> songsWithSameLength = getSongsByLength(length);
                if(!songsWithSameLength.isEmpty()) {
                    playlistSongMap.put(newPlayList, songsWithSameLength); // add list of songs to the newplaylist based on song length
                } else {
                    throw new Exception("There is no Songs Exist in the List.");
                }
                return newPlayList;
            } else {
                return null;
            }
        } else {
            throw new Exception("User does not exist");
        }
    }

    public List<Song> getMatchingSongsList(List<String> songTitles) {
        List<Song> matchingSongsList = new ArrayList<>();
        for(String title: songTitles) {
            for(Song song: songs) {
                if(title.equals(song.getTitle())) {
                    matchingSongsList.add(song);
                    break;
                }
            }
        }
      return matchingSongsList;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = getUserByMobile(mobile);
        if(user != null) {
            Playlist newPlayList = createNewPlayList(mobile, title);
            if(newPlayList != null) {
                creatorPlaylistMap.put(user, newPlayList); // maps user and playlist
                List<Playlist> playListArray = getPlaylistsArray(user);
                if(playListArray != null) {
                    playListArray.add(newPlayList);
                    userPlaylistMap.put(user, playListArray); // add this play list to the users playlist list(appending with existing playlist)
                } else {
                    userPlaylistMap.put(user, addPlayListToPlayListArray(newPlayList)); // // add this play list to the users playlist list(adding)
                }
                playlistListenerMap.put(newPlayList, addUserToUsersArray(user)); // mark the creater as a listner of this playlist
                List<Song> songsWithSameLength = getMatchingSongsList(songTitles);
                if(!songsWithSameLength.isEmpty()) {
                    playlistSongMap.put(newPlayList, songsWithSameLength); // add list of  matching songs to the newplaylist based on given song titles
                } else {
                    throw new Exception("There is no Songs Exist in the List.");
                }
                return newPlayList;
            } else {
                return null;
            }
        } else {
            throw new Exception("User does not exist");
        }
    }
    public Playlist getPlayListByTitle(String playlistTitle) {
        if(!playlists.isEmpty()) {
            for(Playlist playlist: playlists) {
                if(playlist.getTitle().equals(playlistTitle)) return playlist;
            }
            return null;
        } else {
            return null;
        }
    }
    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist playlist = getPlayListByTitle(playlistTitle);
        if(playlist != null) {
            List<User> users = playlistListenerMap.get(playlist);
            User user = getUserByMobile(mobile);
            if(user != null) {
                if(!users.contains(user)) {
                    users.add(user);
                    playlistListenerMap.put(playlist, users);
                    return playlist;
                } else {
                    throw new Exception("Creator or Listner alredy exist in the list.");
                }
            } else {
                throw new Exception("User does not exist");
            }
        } else {
            throw new Exception("Playlist does not exist");
        }
    }

    public Song updateLikeInSongs(Song song) {
        if(!songs.isEmpty()) {
            for(Song songData: songs) {
                if(songData.equals(song)) {
                    songData.setLikes(songData.getLikes()+1);
                    return songData;
                }
            }
            return null;
        } else {
            return null;
        }
    }
    public Album findAlbumBySong(Song targetSong) {
        for (Map.Entry<Album, List<Song>> entry : albumSongMap.entrySet()) {
            Album album = entry.getKey();
            List<Song> songs = entry.getValue();
            if (songs.contains(targetSong)) {
                return album;
            }
        }
        return null;
    }
    public Artist findArtistByAlbum(Album targetAlbum) {
        for (Map.Entry<Artist, List<Album>> entry : artistAlbumMap.entrySet()) {
            Artist artist = entry.getKey();
            List<Album> albumList = entry.getValue();
            if (albumList.contains(targetAlbum)) {
                return artist;
            }
        }
        return null;
    }

    public Artist updateLikeInArtist(Artist artist) {
        if(!artists.isEmpty()) {
            for(Artist artistData: artists) {
                if(artistData.equals(artist)) {
                    artistData.setLikes(artistData.getLikes()+1);
                    return artistData;
                }
            }
            return null;
        } else {
            return null;
        }
    }
    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = getUserByMobile(mobile);
        if(user != null) {
            Song song = getSongByName(songTitle);
            if(song != null) {
                List<User> users = songLikeMap.get(song);
                if(users == null || !users.contains(user)) {
                    Album album = findAlbumBySong(song);
                    if(album != null) {
                        Artist artist = findArtistByAlbum(album);
                        if(artist != null) {
                            Song updatedSong = updateLikeInSongs(song);
                            Artist updatedArtist = updateLikeInArtist(artist);
                            if(updatedSong != null && updatedArtist != null) {
                                if(users == null || users.isEmpty()) {
                                    List<User> newUsersList = new ArrayList<>();
                                    newUsersList.add(user);
                                    songLikeMap.put(updatedSong, newUsersList);
                                } else {
                                    users.add(user);
                                    songLikeMap.put(updatedSong, users);
                                }
                                return updatedSong;
                            } else {
                                throw new Exception("Artist or Song not updated");
                            }
                        } else {
                            throw new Exception("can't find artist for this song");
                        }
                    } else {
                        throw new Exception("can't find album for given song");
                    }
                } else {
//                    throw new Exception("User Already liked this song.");
                    return null;
                }
            } else {
                throw new Exception("Song does not exist");
            }
        } else {
            throw new Exception("User does not exist");
        }
    }

    public String mostPopularArtist() {
        Artist artistWithMaxLikes = Collections.max(artists, Comparator.comparingInt(Artist::getLikes));
        if(artistWithMaxLikes != null) {
            return "Artist Name: "+artistWithMaxLikes.getName()+" likes: "+ artistWithMaxLikes.getLikes();
        }
        return "";
    }

    public String mostPopularSong() {
        Song songWithMaxLikes = Collections.max(songs, Comparator.comparingInt(Song::getLikes));
        if(songWithMaxLikes != null) {
            return "Song Title: "+songWithMaxLikes.getTitle()+" likes: "+ songWithMaxLikes.getLikes();
        }
        return "";
    }
}
