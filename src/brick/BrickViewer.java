package brick;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.threed.jpct.Config;
import com.threed.jpct.Matrix;

@SuppressWarnings("serial")
public class BrickViewer extends JFrame
{
	// private final static String myPath = "~/books/robotics";
	public final static String ldrawPath = "ldraw";
	private AdjustmentPane ap;

	public BrickViewer(String filename) throws PartNotFoundException, FileNotFoundException
	{
		PartFactory fact = new PartFactory(ldrawPath);
		// fact.addPath(myPath);
		PartSpec model = fact.getPart(filename);

		Config.maxPolysVisible = 100000;

		
		setTitle("Brick Viewer");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		BrickPanel bp = new BrickPanel();
		getContentPane().add(bp);

		// Make two of the same brick
		BrickObject obj = model.toBrickObject(new Matrix(), bp);
		bp.addBrick(obj);
		BrickObject obj2 = model.toBrickObject(new Matrix(), bp);
		bp.addBrick(obj2);
		// Put them side by side.
		obj.translateZ(-20f);
		obj2.translateZ(20f);
		// Make them different colors.
		obj.setColorCode(2);
		obj2.setColorCode(1);

		bp.setSelectedBrick(obj);
		bp.buildAll();

		// System.out.println("Original Pivot:  " + obj2.getRotationPivot());
		// System.out.println("Original Center: " + obj2.getCenter());
		// System.out.println("Original TCent:  " +
		// obj2.getTransformedCenter());
		// obj .setRotationPivot(new SimpleVector(10, 0, 10));
		// obj2.setRotationPivot(new SimpleVector(10, 0, 10));
		// System.out.println("Object's name is " + obj.getName());
		// System.out.println("Object2's name is " + obj2.getName());

		addKeyListener(new BrickKeyController((float) Math.PI / 4, bp));
		bp.setupDefaultListeners();
		ap = new AdjustmentPane(bp);
		bp.setAdjustmentPane(ap);
		ap.setSize(400, 290);
		ap.updateSelectedObject(obj);
	}

	public void setVisible(boolean b)
	{
		super.setVisible(b);
		ap.setVisible(b);

		ap.updatePositions();
		Point apPos = ap.getLocation();
		apPos.x += ap.getWidth();
		setLocation(apPos);
	}

	public static void main(String[] args)
	{
		String loc = "";
		if (args.length < 1)
		{
			System.out.println("Usage: BrickViewer model");
			JFileChooser jfc = new JFileChooser(new File(ldrawPath));
			FileNameExtensionFilter fnef = new FileNameExtensionFilter("LDraw model files (.dat)", "dat");
			jfc.setFileFilter(fnef);
			int ret = jfc.showOpenDialog(null);
			if (ret == JFileChooser.APPROVE_OPTION)
			{
				loc = jfc.getSelectedFile().getName();
				System.out.println(loc);
			} else
			{
				System.exit(1);
			}
		} else
		{
			loc = args[0];
		}
		try
		{
			new BrickViewer(loc).setVisible(true);
		} catch (PartNotFoundException pnfe)
		{
			JOptionPane.showMessageDialog(null, "Cannot find model \"" + loc + "\"");
		} catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Cannot locate ldconfig.ldr");
		}
	}
}
