# ImageToText App for Android

<a href="https://imgflip.com/gif/3oc3gr"><img src="https://i.imgflip.com/3oc3gr.gif" title="Demo"/></a>

## Purpose:

Application is designed to extract text from an image from an in-built gallery and use the acquired text ouput either to save as a note in the application itself or can be used to share with other applications on an android device such e-mail, text message and so on.

Process:

  * The application start with a splash activity and then opens in an empty List View. User can access in-app gallery or camera mode with buttons available at the top end. 
  * Once image chosen, the image is set in full view activity for the user to crop the image to desired area where text is present.
  * The cropped image is then passed on to the next activity where the image is scaled using seeked bar. The reason for this was to amplify the accuracy of text recognition. 
  * User can then process the image using the “Process Text” button.
  * The output is generated in the form of a string to be set in the next activity which has two fields, one for the note name and second for the output. 
  * From here, the note can be saved or shared with other applications on the device.

### References and Libraries used:



  * [Firebase Image Text Recognition for recognizing text](https://firebase.google.com/docs/ml-kit/android/recognize-text). 
  * [Otsu Method](https://en.wikipedia.org/wiki/Otsu%27s_method) was used to Binarize the image for amplify and improve text recognition.
  * For cropping image used api 'com.theartofdev.edmodo:android-image-cropper:2.8.+'. Refer to github [link](https://github.com/ArthurHub/Android-Image-Cropper)
