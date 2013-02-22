package reactive.ui;

import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

import reactive.ui.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.*;
import android.content.*;
import android.graphics.*;
import tee.binding.properties.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import tee.binding.task.*;
import tee.binding.it.*;
import android.text.*;
import java.nio.*;
import android.opengl.*;

class MyGLRenderer implements GLSurfaceView.Renderer {
	private Pyramid pyramid; // (NEW)
	private Cube cube; // (NEW)
	private static float anglePyramid = 0; // Rotational angle in degree for pyramid (NEW)
	private static float angleCube = 0; // Rotational angle in degree for cube (NEW)
	private static float speedPyramid = 2.0f; // Rotational speed for pyramid (NEW)
	private static float speedCube = -1.5f; // Rotational speed for cube (NEW)
	
	// Constructor
	public MyGLRenderer(Context context) {
		// Set up the buffers for these shapes
		pyramid = new Pyramid(); // (NEW)
		cube = new Cube(); // (NEW)
	}
	// Call back when the surface is first created or re-created.
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// NO CHANGE - SKIP
		//......
	}
	// Call back after onSurfaceCreated() or whenever the window's size changes.
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// NO CHANGE - SKIP
		//......
	}
	// Call back to draw the current frame.
	@Override
	public void onDrawFrame(GL10 gl) {
		//System.out.println("draw");
		// Clear color and depth buffers
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// ----- Render the Pyramid -----
		gl.glLoadIdentity(); // Reset the model-view matrix
		gl.glTranslatef(-1.5f, 0.0f, -6.0f); // Translate left and into the screen
		gl.glRotatef(anglePyramid, 0.1f, 1.0f, -0.1f); // Rotate (NEW)
		pyramid.draw(gl); // Draw the pyramid (NEW)
		// ----- Render the Color Cube -----
		gl.glLoadIdentity(); // Reset the model-view matrix
		gl.glTranslatef(1.5f, 0.0f, -6.0f); // Translate right and into the screen
		gl.glScalef(0.8f, 0.8f, 0.8f); // Scale down (NEW)
		gl.glRotatef(angleCube, 1.0f, 1.0f, 1.0f); // rotate about the axis (1,1,1) (NEW)
		cube.draw(gl); // Draw the cube (NEW)
		// Update the rotational angle after each refresh (NEW)
		anglePyramid += speedPyramid; // (NEW)
		angleCube += speedCube; // (NEW)
	}
}

class Cube {
	private FloatBuffer vertexBuffer; // Buffer for vertex-array
	private int numFaces = 6;
	private float[][] colors = { // Colors of the 6 faces
	{ 1.0f, 0.5f, 0.0f, 1.0f }, // 0. orange
			{ 1.0f, 0.0f, 1.0f, 1.0f }, // 1. violet
			{ 0.0f, 1.0f, 0.0f, 1.0f }, // 2. green
			{ 0.0f, 0.0f, 1.0f, 1.0f }, // 3. blue
			{ 1.0f, 0.0f, 0.0f, 1.0f }, // 4. red
			{ 1.0f, 1.0f, 0.0f, 1.0f } // 5. yellow
	};
	private float[] vertices = { // Vertices of the 6 faces
	// FRONT
			-1.0f, -1.0f, 1.0f, // 0. left-bottom-front
			1.0f, -1.0f, 1.0f, // 1. right-bottom-front
			-1.0f, 1.0f, 1.0f, // 2. left-top-front
			1.0f, 1.0f, 1.0f, // 3. right-top-front
			// BACK
			1.0f, -1.0f, -1.0f, // 6. right-bottom-back
			-1.0f, -1.0f, -1.0f, // 4. left-bottom-back
			1.0f, 1.0f, -1.0f, // 7. right-top-back
			-1.0f, 1.0f, -1.0f, // 5. left-top-back
			// LEFT
			-1.0f, -1.0f, -1.0f, // 4. left-bottom-back
			-1.0f, -1.0f, 1.0f, // 0. left-bottom-front 
			-1.0f, 1.0f, -1.0f, // 5. left-top-back
			-1.0f, 1.0f, 1.0f, // 2. left-top-front
			// RIGHT
			1.0f, -1.0f, 1.0f, // 1. right-bottom-front
			1.0f, -1.0f, -1.0f, // 6. right-bottom-back
			1.0f, 1.0f, 1.0f, // 3. right-top-front
			1.0f, 1.0f, -1.0f, // 7. right-top-back
			// TOP
			-1.0f, 1.0f, 1.0f, // 2. left-top-front
			1.0f, 1.0f, 1.0f, // 3. right-top-front
			-1.0f, 1.0f, -1.0f, // 5. left-top-back
			1.0f, 1.0f, -1.0f, // 7. right-top-back
			// BOTTOM
			-1.0f, -1.0f, -1.0f, // 4. left-bottom-back
			1.0f, -1.0f, -1.0f, // 6. right-bottom-back
			-1.0f, -1.0f, 1.0f, // 0. left-bottom-front
			1.0f, -1.0f, 1.0f // 1. right-bottom-front
	};

	// Constructor - Set up the buffers
	public Cube() {
		// Setup vertex-array buffer. Vertices in float. An float has 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		vertexBuffer.put(vertices); // Copy data into buffer
		vertexBuffer.position(0); // Rewind
	}
	// Draw the shape
	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW); // Front face in counter-clockwise orientation
		gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
		gl.glCullFace(GL10.GL_BACK); // Cull the back face (don't display)
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		// Render all the faces
		for (int face = 0; face < numFaces; face++) {
			// Set the color for each of the faces
			gl.glColor4f(colors[face][0], colors[face][1], colors[face][2], colors[face][3]);
			// Draw the primitive from the vertex-array directly
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face * 4, 4);
		}
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
}

class Pyramid {
	private FloatBuffer vertexBuffer; // Buffer for vertex-array
	private FloatBuffer colorBuffer; // Buffer for color-array
	private ByteBuffer indexBuffer; // Buffer for index-array
	private float[] vertices = { // 5 vertices of the pyramid in (x,y,z)
	-1.0f, -1.0f, -1.0f, // 0. left-bottom-back
			1.0f, -1.0f, -1.0f, // 1. right-bottom-back
			1.0f, -1.0f, 1.0f, // 2. right-bottom-front
			-1.0f, -1.0f, 1.0f, // 3. left-bottom-front
			0.0f, 1.0f, 0.0f // 4. top
	};
	private float[] colors = { // Colors of the 5 vertices in RGBA
	0.0f, 0.0f, 1.0f, 1.0f, // 0. blue
			0.0f, 1.0f, 0.0f, 1.0f, // 1. green
			0.0f, 0.0f, 1.0f, 1.0f, // 2. blue
			0.0f, 1.0f, 0.0f, 1.0f, // 3. green
			1.0f, 0.0f, 0.0f, 1.0f // 4. red
	};
	private byte[] indices = { // Vertex indices of the 4 Triangles
	2, 4, 3, // front face (CCW)
			1, 4, 2, // right face
			0, 4, 1, // back face
			4, 0, 3 // left face
	};

	// Constructor - Set up the buffers
	public Pyramid() {
		// Setup vertex-array buffer. Vertices in float. An float has 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder()); // Use native byte order
		vertexBuffer = vbb.asFloatBuffer(); // Convert from byte to float
		vertexBuffer.put(vertices); // Copy data into buffer
		vertexBuffer.position(0); // Rewind
		// Setup color-array buffer. Colors in float. An float has 4 bytes
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
		// Setup index-array buffer. Indices in byte.
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	// Draw the shape
	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CCW); // Front face in counter-clockwise orientation
		// Enable arrays and define their buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}

public class OpenGL2 extends GLSurfaceView implements Rake {
	private ToggleProperty<Rake> hidden = new ToggleProperty<Rake>(this);
	boolean initialized = false;
	private NumericProperty<Rake> width = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> height = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> left = new NumericProperty<Rake>(this);
	private NumericProperty<Rake> top = new NumericProperty<Rake>(this);
	//private GLSurfaceView glView; // Use GLSurfaceView
	Paint paint = new Paint();
	Task reFit = new Task() {
		@Override
		public void doTask() {
			//System.out.
			if (width.property.value() < 1 || height.property.value() < 1) {
				return;
			}
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(//
					width.property.value().intValue()//
					, height.property.value().intValue());
			params.leftMargin = (int) (left.property.value() + 0);
			params.topMargin = (int) (top.property.value() + 0);
			OpenGL2.this.setLayoutParams(params);
			//System.out.println("params.topMargin: " + params.topMargin+" / "+Decor.this.getLeft()+"x"+Decor.this.getTop()+"/"+Decor.this.getWidth()+"x"+Decor.this.getHeight());
		}
	};

	void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		width.property.afterChange(reFit).value(100);
		height.property.afterChange(reFit).value(100);
		left.property.afterChange(reFit);
		top.property.afterChange(reFit);
		paint.setColor(0xff660000);
		//glView = new GLSurfaceView(this.getContext()); // Allocate a GLSurfaceView
		//glView.setRenderer(new MyGLRenderer(this.getContext())); // Use a custom renderer
		//reFit.start();
		setRenderer(new MyGLRenderer(this.getContext()));
		hidden.property.afterChange(new Task() {
			@Override
			public void doTask() {
				if (hidden.property.value()) {
					setVisibility(View.INVISIBLE);
				}
				else {
					setVisibility(View.VISIBLE);
				}
			}
		});
	}
	@Override
	public ToggleProperty<Rake> hidden() {
		return hidden;
	}
	public OpenGL2(Context context) {
		super(context);
		init();
	}
	public OpenGL2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	/*@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int w = width.property.value().intValue();
		int h = height.property.value().intValue();
		int x = 0;//left.property.value().intValue();
		int y = 0;//top.property.value().intValue();
		System.out.println("srbseb");
		canvas.drawRoundRect(new RectF(x, y, x + w, y + h)//
				, 4//
				, 4//
				, paint);
		//glView.draw(canvas);
	}*/
	@Override
	public NumericProperty<Rake> left() {
		return left;
	}
	@Override
	public NumericProperty<Rake> top() {
		return top;
	}
	@Override
	public NumericProperty<Rake> width() {
		return width;
	}
	@Override
	public NumericProperty<Rake> height() {
		return height;
	}
	@Override
	public View view() {
		return this;
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
