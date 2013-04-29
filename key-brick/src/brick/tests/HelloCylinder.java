package brick.tests;

import com.threed.jpct.*;
import javax.swing.*;

public class HelloCylinder {

	private World world;
	private FrameBuffer buffer;
	private Object3D box;
	private JFrame frame;

	public static void main(String[] args) throws Exception {
		new HelloCylinder().loop();
	}

	public HelloCylinder() throws Exception {
		
		frame=new JFrame("Hello world");
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		world = new World();
		world.setAmbientLight(0, 255, 0);

		TextureManager.getInstance().addTexture("box", new Texture("box.jpg"));

		box = Primitives.getCylinder(10);
		box.setEnvmapped(Object3D.ENVMAP_ENABLED);
		box.build();
		world.addObject(box);
		box.rotateX((float)Math.PI/2);

		world.getCamera().setPosition(0, 0, -50);
		world.getCamera().lookAt(box.getTransformedCenter());
	}

	private void loop() throws Exception {
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);

		while (frame.isShowing()) {
			//box.rotateY(0.01f);
			buffer.clear(java.awt.Color.BLUE);
			world.renderScene(buffer);
			world.draw(buffer);
			buffer.update();
			buffer.display(frame.getGraphics());
			Thread.sleep(10);
		}
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		frame.dispose();
		System.exit(0);
	}
}

	
