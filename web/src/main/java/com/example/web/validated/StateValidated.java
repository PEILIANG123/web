package com.example.web.validated;

import com.example.web.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public  class StateValidated implements ConstraintValidator<State,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s==null) {return false;}
        if(s.equals("已发布") || s.equals("草稿")){ return true;}
        return false;
    }
}
