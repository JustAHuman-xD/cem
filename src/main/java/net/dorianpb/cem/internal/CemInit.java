package net.dorianpb.cem.internal;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class CemInit implements ClientModInitializer{
	private static final Gson gson = new Gson();
	private static final ArrayList<String> BlockEntities = new ArrayList<>(Arrays.asList("book",
	                                                                                     "head_dragon",
	                                                                                     "head_player",
	                                                                                     "head_skeleton",
	                                                                                     "head_wither_skeleton",
	                                                                                     "head_zombie",
	                                                                                     "sign",
	                                                                                     "mob_spawner",
	                                                                                     "piston",
	                                                                                     "chest",
	                                                                                     "ender_chest",
	                                                                                     "trapped_chest",
	                                                                                     "enchanting_table",
	                                                                                     "lectern",
	                                                                                     "end_portal",
	                                                                                     "end_gateway",
	                                                                                     "beacon",
	                                                                                     "skull",
	                                                                                     "banner",
	                                                                                     "structure_block",
	                                                                                     "shulker_box",
	                                                                                     "bed",
	                                                                                     "conduit",
	                                                                                     "bell",
	                                                                                     "campfire"
	                                                                                    ));
	
	@Override
	public void onInitializeClient(){
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener(){
			
			@Override
			public Identifier getFabricId(){
				return new Identifier("dorianpb", "cem_models");
			}
			
			@Override
			public void apply(ResourceManager manager){
				CemFairy.restoreModels();
				for(Identifier id : manager.findResources("cem", this::checkExt)){
					CemFairy.getLogger().info(id.toString());
					try(InputStream stream = manager.getResource(id).getInputStream()){
						@SuppressWarnings("unchecked") LinkedTreeMap<String, Object> file = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
						                                                                                  LinkedTreeMap.class
						                                                                                 );
						if(file == null){
							throw new Exception("Invalid File");
						}
						if(id.getPath().endsWith(".jem")){
							if(BlockEntities.contains(id.getPath().substring(4, id.getPath().length() - 4))){ //check if it is a block entity
								//noinspection all
								BlockEntityRenderDispatcher.INSTANCE.equals(null); //poke it so it wakes up and builds the models
							}
							CemFairy.getRendererFromId(id.toString()).apply(new CemModelRegistry(file, id.getPath()));
						}
						else{ //ends with .jpm
							CemFairy.addJpmFile(file, id.getPath());
						}
					} catch(Exception exception){
						CemFairy.getLogger().error("Error parsing " + id + ":");
						String message = exception.getMessage();
						CemFairy.getLogger().error(exception);
						if(message == null || message.trim().equals("")){
							CemFairy.getLogger().error(exception.getStackTrace()[0]);
							CemFairy.getLogger().error(exception.getStackTrace()[1]);
							CemFairy.getLogger().error(exception.getStackTrace()[2]);
						}
					}
				}
			}
			
			private boolean checkExt(String path){
				return path.endsWith(".jem") || path.endsWith(".jpm");
			}
		});
	}
}


//TODO write documentation for everything so people can adopt the mod and use it like a good boi
//TODO restrict access to as many things as possible