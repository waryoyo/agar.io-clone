package org.example.agarioclone.components;

import com.almasb.fxgl.entity.component.Component;
import org.example.agarioclone.Utility;


public class IDCustomComponent extends Component {
    public String id;
    public IDCustomComponent() {
        id = Utility.getRandomId();
    }
    public IDCustomComponent(String id) {
        this.id = id;
    }
}
