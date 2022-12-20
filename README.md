## Introduction

The PodPlay app will allow searching and subscribing to podcasts from iTunes and provide a playback interface with speed controls.
The following new topics are covered:
1. Android networking.
2. Retrofit REST API library.
3. XML Parsing.
4. Search activity.
5. MediaPlayer library.
6. Room database.

The user has the ability to search for podcasts and displaying the podcast episodes, subscribe to favorite podcasts

## Functionalities

PodPlay will contain these main features:
1. Quick searching of podcasts by keyword or name.
2. Display for previewing podcast episodes.
3. Playback of audio and video podcasts.
4. Subscribing to your favorite podcasts and notify when new episodes are available (check for new episodes in the background and post a notification if any are found).
5. Playback at various speeds.
6. The user can tap on an episode to start it playing and a notification icon will be displayed in the status bar. He could also tap on the pause button to pause the playback.

## Glide library

This app uses Glide library to load smoothly the image.
The user can tap the search icon and enter a search term. The results are displayed and the cover art images load in after the main content is rendered. 
If your search returns enough results, the user can scroll through the list as quickly as possible, and notice that the movement remains smooth no matter how many results and images are loading.
This is handled by the library