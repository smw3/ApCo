uniform sampler2D texSampler;

void main() {
    gl_FragColor = texture2D(texSampler, vec2(gl_TexCoord[0]));
}