#version 430 compatibility

precision highp float;

uniform vec2 Point1;
uniform vec2 Point2;
uniform vec2 Point3;
uniform float Thickness;
uniform float SmoothR;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;

float det(vec2 a, vec2 b)
{
    return a.x * b.y - b.x * a.y;
}

vec2 closestPointInSegment(vec2 a, vec2 b)
{
    vec2 ba = b - a;
    return a + ba*clamp(-dot(a, ba)/dot(ba, ba), 0.0, 1.0);
}

vec2 get_distance_vector(vec2 b0, vec2 b1, vec2 b2) {

    float a = det(b0, b2), b = 2.0 * det(b1, b0), d = 2.0 * det(b2, b1);
    if (abs(2.0*a+b+d) < /*1000.0*/ 0.0001) return closestPointInSegment(b0, b2);

    float f = b * d - a * a;// 𝑓(𝑝)
    vec2 d21 = b2 - b1, d10 = b1 - b0, d20 = b2 - b0;
    vec2 gf = 2.0 * (b * d21 + d * d10 + a * d20);
    gf = vec2(gf.y, -gf.x);// ∇𝑓(𝑝)
    vec2 pp = -f * gf / dot(gf, gf);// 𝑝′
    vec2 d0p = b0 - pp;// 𝑝′ to origin
    float ap = det(d0p, d20), bp = 2.0 * det(d10, d0p);// 𝛼,𝛽(𝑝′)
    // (note that 2*ap+bp+dp=2*a+b+d=4*area(b0,b1,b2))
    float t = clamp((ap + bp)/(2.0 * a + b + d), 0.0, 1.0);
    return mix(mix(b0, b1, t), mix(b1, b2, t), t);// 𝑣𝑖=𝑏(𝑡̅)
}

float approx_distance(vec2 p, vec2 b0, vec2 b1, vec2 b2) {
    return length(get_distance_vector(b0 - p, b1 - p, b2 - p));
}

void main()
{
    if (vertexColor.a == 0.0) {
        discard;
    }
    float d = approx_distance(position, Point1, Point2, Point3);
    float a = 1.0 - smoothstep(Thickness - SmoothR, Thickness, d);
    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, a);
}


/*void main()
{
    float d = approx_distance(position, point1, point2, point3);
    float a;
    if (d < thickness) {
        a = 1.0;
    } else {
        //Anti-alias the edge.
        a = 1.0 - smoothstep(d, thickness, thickness+0.5);
    }
    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, a);
}


void main()
{
    float d = approx_distance(position, point1, point2, point3);
    float a;
    if (d < thickness) {
        a = 1.0;
    } else {
        //Anti-alias the edge.
        a = 1.0 - smoothstep(d, thickness, thickness+0.5);
    }
    fragColor = vertexColor * vec4(1.0, 1.0, 1.0, a);
}*/


/*void main() {
    vec2 p0 = position;
    float a = point3.x - 2 * point2.x + point1.x;
    float b = 2 * (point2.x - point1.x);
    float c = point1.x - p0.x;
    float dx = b * b - 4.0f * a * c;

    if (dx >= 0.0f) {
        float t1 = (-b + sqrt(dx)) / (2 * a);
        float t2 = (-b - sqrt(dx)) / (2 * a);
        float y1 = point1.y + 2 * t1 * (point2.y - point1.y) + t1 * t1 * (point3.y - 2 * point2.y + point1.y);
        float y2 = point1.y + 2 * t2 * (point2.y - point1.y) + t2 * t2 * (point3.y - 2 * point2.y + point1.y);
        if ((0.0f <= t1 && t1 <= 1.0f && abs(p0.y - y1) < thickness) || (0.0f <= t2 && t2 <= 1.0f && abs(p0.y - y2) < thickness))
        {
            float r1 = abs(p0.y - y1) / thickness;
            float r2 = abs(p0.y - y2) / thickness;

            if (0.0 <= r1 && r1 <= 1.0) {
                fragColor = vec4(0.0, 0.75, 0.0, smoothstep(1.0, 0.0, r1));
            }

            if (0.0 <= r2 && r2 <= 1.0) {
                fragColor = vec4(0.0, 0.75, 0.0, smoothstep(1.0, 0.0, r2));
            }
        }
        else
        {
            fragColor = vec4(0.0);
        }
    }
    else
    {
        fragColor = vec4(0.0);
    }
}*/


