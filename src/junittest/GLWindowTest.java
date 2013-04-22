package junittest;

import java.io.FileNotFoundException;
import org.lwjgl.LWJGLException;
import brick.PartNotFoundException;
import glbrick.GLWindow;

public class GLWindowTest {
	public static void main(String[] args) throws LWJGLException, InterruptedException, PartNotFoundException, FileNotFoundException, glbrick.PartNotFoundException
    {
            GLWindow window = new GLWindow();
            window.run();
    }
}
