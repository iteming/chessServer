package com.example.chess.entity.five;

import com.example.chess.entity.base.Action;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by chanming on 16/7/13.
 */

@EqualsAndHashCode(callSuper = true)
@Data
class StartAction extends Action {
    StartAction(){
        this.code = "Start";
    }
}
