package kore.botssdk.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BitmapUtilsTest {

    @Test
    public void testGetExtensionFromFileName() {
        assertEquals("jpg", BitmapUtils.getExtensionFromFileName("test.jpg"));
        assertEquals("png", BitmapUtils.getExtensionFromFileName("image.png"));
        assertEquals("txt", BitmapUtils.getExtensionFromFileName("notes.txt"));
        assertEquals("", BitmapUtils.getExtensionFromFileName("noextension"));
        assertEquals("", BitmapUtils.getExtensionFromFileName(null));
    }

    @Test
    public void testCategorisedAttachmentType() {
        assertEquals(BitmapUtils.TYPE_IMAGE_ATTACHMENT, BitmapUtils.categorisedAttachmentType("jpg"));
        assertEquals(BitmapUtils.TYPE_IMAGE_ATTACHMENT, BitmapUtils.categorisedAttachmentType("PNG"));
        assertEquals(BitmapUtils.TYPE_PDF_ATTACHMENT, BitmapUtils.categorisedAttachmentType("pdf"));
        assertEquals(BitmapUtils.TYPE_VIDEO_ATTACHMENT, BitmapUtils.categorisedAttachmentType("mp4"));
        assertEquals(BitmapUtils.TYPE_AUDIO_ATTACHMENT, BitmapUtils.categorisedAttachmentType("mp3"));
        assertEquals(BitmapUtils.TYPE_TEXT, BitmapUtils.categorisedAttachmentType("txt"));
        assertEquals(BitmapUtils.TYPE_OTHER_ATTACHMENT, BitmapUtils.categorisedAttachmentType("unknown"));
    }
}
