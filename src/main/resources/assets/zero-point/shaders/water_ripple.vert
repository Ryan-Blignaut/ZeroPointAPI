#version 430 compatibility

in vec2 Position;
in vec4 Color;
in vec2 UV;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;


out vec4 textureCoordRipple;
uniform float Time;
uniform float ScrollSpeed;// {"material":"Scroll speed","default":0,"range":[0,0.5]}
uniform float Direction;// {"material":"Scroll direction","default":0,"range":[0,6.28]}
uniform float AnimationSpeed;// {"material":"Animation speed","default":0.15,"range":[0,0.5]}
uniform float Scale;// {"material":"Ripple scale","default":1,"range":[0,10]}

uniform vec4 Texture0Res;
uniform vec4 Texture1Res;


smooth out vec2 position;
smooth out vec4 vertexColor;
smooth out vec2 textureCoord;


vec2 rotate(vec2 v, float a) {
	float s = sin(a);
	float c = cos(a);
	mat2 m = mat2(c, -s, s, c);
	return m * v;
}

void main() {
	gl_Position = ProjMat *  ModelViewMat * vec4(Position, 1.0, 1.0);
	position = Position.xy;
	vertexColor = Color;
	textureCoord = UV;

	float piFrac = 0.78539816339744830961566084581988 * 0.5;
	float pi = 3.141;
	vec2 coordsRotated = textureCoord.xy;
	vec2 coordsRotated2 = textureCoord.xy * 1.333;

	vec2 scroll = rotate(vec2(0, -1), Direction) * ScrollSpeed * ScrollSpeed * Time;

	textureCoordRipple.xy = coordsRotated + Time * AnimationSpeed * AnimationSpeed + scroll;
	textureCoordRipple.zw = coordsRotated2 - Time * AnimationSpeed * AnimationSpeed + scroll;
	textureCoordRipple *= Scale;

	float rippleTextureAdjustment = (Texture0Res.x / Texture0Res.y);
	//	textureCoordRipple.xz *= rippleTextureAdjustment;

	//	textureCoord.zw = vec2(textureCoord.x * Texture1Res.z / Texture1Res.x, textureCoord.y * Texture1Res.w / Texture1Res.y);

}