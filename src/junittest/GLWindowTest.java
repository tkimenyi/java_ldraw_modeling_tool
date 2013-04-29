package junittest;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import glbrick.GLWindow;
import glbrick.MalformedLDrawException;

public class GLWindowTest {
	public static void main(String[] args) throws LWJGLException, InterruptedException, glbrick.PartNotFoundException, MalformedLDrawException, IOException
    {
            GLWindow window = new GLWindow();
            window.run();
    }
}
