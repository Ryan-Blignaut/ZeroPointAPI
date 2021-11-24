#version 430 compatibility

precision highp float;

smooth in vec2 position;
//smooth in vec4 vertexColor;

uniform float Hue;

out vec4 fragColor;

float interpolate(float one, float two, float point){
    float interpolated = one + (two - one)*point;
    return interpolated;
}

float map(float value, float fromMin, float fromMax, float toMin, float toMax){
    float percentage = (value - fromMin)/(fromMax - fromMin);
    return toMin + (toMax - toMin)*percentage;
}

vec4 hueToRGB(float hue){
    float red = 0.0;
    float green = 0.0;
    float blue = 0.0;
    if (hue >= 0.0 && hue < 60.0){
        red = 1.0;
        green = hue/60.0;
        blue = 0.0;
    } else if (hue >= 60.0 && hue < 120.0){
        red = 1.0 - (hue - 60.0)/60.0;
        green = 1.0;
        blue = 0.0;
    } else if (hue >= 120.0 && hue < 180.0){
        red = 0.0;
        green = 1.0;
        blue = (hue - 120.0)/60.0;
    } else if (hue >= 180.0 && hue < 240.0){
        red = 0.0;
        green = 1.0 - (hue - 180.0)/60.0;
        blue = 1.0;
    } else if (hue >= 240.0 && hue < 300.0){
        red = (hue - 240.0)/60.0;
        green = 0.0;
        blue = 1.0;
    } else if (hue >= 300.0 && hue <= 360.0){
        red = 1.0;
        green = 0.0;
        blue = 1.0 - (hue - 300.0)/60.0;
    }
    return vec4(red, green, blue, 1);
}



void main()
{
    float hue1 =  position.y;//map(position.y, 0.1, 1.0, 360.0, 0.0);

    fragColor = pow(hueToRGB(hue1), vec4(1.0/2.2));
    //fragColor = vertexColor * vec4(1.0, 1.0, 1.0, 1.0);
}