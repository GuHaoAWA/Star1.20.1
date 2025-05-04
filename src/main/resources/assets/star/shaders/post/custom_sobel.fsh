#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InverseSize;
uniform float intensity;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 texel = texture(DiffuseSampler, texCoord).rgb;
    
    if (intensity < 0.01) {
        fragColor = vec4(texel, 1.0);
        return;
    }
    
    // Sobel边缘检测算法
    vec3 topLeft = texture(DiffuseSampler, texCoord + vec2(-1.0, -1.0) * InverseSize).rgb;
    vec3 top = texture(DiffuseSampler, texCoord + vec2(0.0, -1.0) * InverseSize).rgb;
    vec3 topRight = texture(DiffuseSampler, texCoord + vec2(1.0, -1.0) * InverseSize).rgb;
    vec3 left = texture(DiffuseSampler, texCoord + vec2(-1.0, 0.0) * InverseSize).rgb;
    vec3 right = texture(DiffuseSampler, texCoord + vec2(1.0, 0.0) * InverseSize).rgb;
    vec3 bottomLeft = texture(DiffuseSampler, texCoord + vec2(-1.0, 1.0) * InverseSize).rgb;
    vec3 bottom = texture(DiffuseSampler, texCoord + vec2(0.0, 1.0) * InverseSize).rgb;
    vec3 bottomRight = texture(DiffuseSampler, texCoord + vec2(1.0, 1.0) * InverseSize).rgb;
    
    vec3 sx = -topLeft - 2.0 * left - bottomLeft + topRight + 2.0 * right + bottomRight;
    vec3 sy = -topLeft - 2.0 * top - topRight + bottomLeft + 2.0 * bottom + bottomRight;
    vec3 edge = sqrt(sx * sx + sy * sy);
    
    // 混合原始颜色和边缘检测结果
    fragColor = vec4(mix(texel, edge, intensity), 1.0);
}