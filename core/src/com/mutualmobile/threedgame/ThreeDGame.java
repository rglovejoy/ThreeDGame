package com.mutualmobile.threedgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

public class ThreeDGame extends ApplicationAdapter {

	private static final int XARRAY = 5;
	private static final int YARRAY = 5;
	private static final int ZARRAY = 5;
	private static final float SEPARATION = 10;


	private CameraInputController cameraInputController;
	public PerspectiveCamera camera;

	public Environment environment;



	public Model sphere;
	public Array<ModelInstance> modelInstances = new Array<ModelInstance>();
	public ModelBatch modelBatch;

	public boolean loading;


	@Override
	public void create () {
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.add(new DirectionalLight().set(0.9f, 0.9f, 0.9f, -1, -2, -3));

		loading = true;

		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(50f, 50f, 50f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();

		cameraInputController = new CameraInputController(camera);
		Gdx.input.setInputProcessor(cameraInputController);

				ModelBuilder modelBuilder = new ModelBuilder();

		sphere = modelBuilder.createSphere(1f, 1f, 1f, 20, 20,
				new Material(ColorAttribute.createDiffuse(Color.ORANGE)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
	}

	private void doneLoading() {
		for (int i = 0; i < XARRAY * YARRAY * ZARRAY; i++) {
			ModelInstance instance = new ModelInstance(sphere);
			modelInstances.add(instance);
		}

		loading = false;
	}

	@Override
	public void render () {
		if (loading) {
			doneLoading();
		}

		cameraInputController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		for (int z = 0; z < ZARRAY; z++) {
			for (int y = 0; y < YARRAY; y++) {
				for (int x = 0; x < XARRAY; x++) {
					modelInstances.get(XARRAY * YARRAY * z + XARRAY * y + x).
							transform.setToTranslation((x - XARRAY / 2) * SEPARATION,
									(y - YARRAY / 2) * SEPARATION,
									(z - ZARRAY / 2) * SEPARATION);
				}
			}
		}


		modelBatch.begin(camera);
		modelBatch.render(modelInstances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		modelInstances.clear();
		sphere.dispose();
	}
}
