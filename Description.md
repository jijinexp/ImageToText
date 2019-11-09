# ImageToText
Purpose:

Application is designed to extract text from an image from an in-built gallery and use the acquired text ouput either to save as a note in the application itself or can be used to share with other applications on an android device such e-mail, text message and so on.


Process:

    1. The application start with a splash activity and then opens in an empty List View. User can access in-app gallery or camera mode with buttons available at the top end. 
    2. Once image chosen, the image is set in full view activity for the user to crop the image to desired area where text is present.
    3. The cropped image is then passed on to the next activity where the image is scaled using seeked bar. The reason for this was to amplify the accuracy of text recognition. 
    4. User can then process the image using the “Process Text” button.
    5. The output is generated in the form of a string to be set in the next activity which has two fields, one for the note name and second for the output. 
    6. From here, the note can be saved or shared with other applications on the device.


Methods, Libraries and Previous Code used:

I have used code from assignment 2 for the gallery app in this application.

In terms of Libraries, 
    • Firebase Image Text Recognition for recognizing text.
    • Otsu Method was used to Binarize the image for amplify and improve text recognition.Please see https://en.wikipedia.org/wiki/Otsu%27s_method
    • For cropping image used api 'com.theartofdev.edmodo:android-image-cropper:2.8.+'.
	Please see https://github.com/ArthurHub/Android-Image-Cropper
