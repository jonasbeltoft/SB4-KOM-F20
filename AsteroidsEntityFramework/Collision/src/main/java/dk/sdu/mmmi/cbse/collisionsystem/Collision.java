package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class Collision implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        final List<Entity> entities = new ArrayList<>(world.getEntities());

        for (int i = 0; i < entities.size()-1; i++) {
            Entity entity1 = entities.get(i);
            LifePart ent1Life = entity1.getPart(LifePart.class);

            for (int j = i+1; j < entities.size() ; j++) {
                Entity entity2 = entities.get(j);

                if (!isColliding(entity1,entity2)){
                    continue;
                }

                ent1Life.setLife(ent1Life.getLife()-1);
                if (ent1Life.getLife() < 1){
                    world.removeEntity(entity1);
                }

                LifePart ent2Life = entity2.getPart(LifePart.class);
                ent2Life.setLife(ent2Life.getLife()-1);
                if (ent2Life.getLife() < 1){
                    world.removeEntity(entity2);
                }
            }
        }
    }

    private boolean isColliding(Entity entity1, Entity entity2){
        PositionPart entity1_pos = entity1.getPart(PositionPart.class);
        PositionPart entity2_pos = entity2.getPart(PositionPart.class);

        float dx = entity1_pos.getX() - entity2_pos.getX();
        float dy = entity1_pos.getY() - entity2_pos.getY();
        float distance = (float) Math.sqrt(dx*dx + dy*dy);

        return distance < entity1.getRadius() + entity2.getRadius();
    }
}
