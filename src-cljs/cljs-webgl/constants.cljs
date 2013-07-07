(ns cljs-webgl.constants)
; ClearBufferMask
(def depth-buffer-bit 0x00000100)
(def stencil-buffer-bit 0x00000400)
(def color-buffer-bit 0x00004000)


; BeginMode
(def points 0x0000)
(def lines 0x0001)
(def line-loop 0x0002)
(def line-strip 0x0003)
(def triangles 0x0004)
(def triangle-strip 0x0005)
(def triangle-fan 0x0006)


; BlendingFactorDest
(def zero 0)
(def one 1)
(def src-color 0x0300)
(def one-minus-src-color 0x0301)
(def src-alpha 0x0302)
(def one-minus-src-alpha 0x0303)
(def dst-alpha 0x0304)
(def one-minus-dst-alpha 0x0305)


; BlendingFactorSrc
(def dst-color 0x0306)
(def one-minus-dst-color 0x0307)
(def src-alpha-saturate 0x0308)


; BlendEquationSeparate
(def func-add 0x8006)
(def blend-equation 0x8009)
(def blend-equation-rgb 0x8009) ; Same as blend-equation
(def blend-equation-alpha 0x883D)


; BlendSubtract
(def func-subtract 0x800A)
(def func-reverse-subtract 0x800B)


; Separate Blend Functions
(def blend-dst-rgb 0x80C8)
(def blend-src-rgb 0x80C9)
(def blend-dst-alpha 0x80CA)
(def blend-src-alpha 0x80CB)
(def constant-color 0x8001)
(def one-minus-constant-color 0x8002)
(def constant-alpha 0x8003)
(def one-minus-constant-alpha 0x8004)
(def blend-color 0x8005)


; Buffer Objects
(def array-buffer 0x8892)
(def element-array-buffer 0x8893)
(def array-buffer-binding 0x8894)
(def element-array-buffer-binding 0x8895)

(def stream-draw 0x88E0)
(def static-draw 0x88E4)
(def dynamic-draw 0x88E8)

(def buffer-size 0x8764)
(def buffer-usage 0x8765)

(def current-vertex-attrib 0x8626)


; CullFaceMode
(def front 0x0404)
(def back 0x0405)
(def front-and-back 0x0408)


; EnableCap
(def cull-face 0x0B44)
(def blend 0x0BE2)
(def dither 0x0BD0)
(def stencil-test 0x0B90)
(def depth-test 0x0B71)
(def scissor-test 0x0C11)
(def polygon-offset-fill 0x8037)
(def sample-alpha-to-coverage 0x809E)
(def sample-coverage 0x80A0)


; ErrorCode
(def no-error 0)
(def invalid-enum 0x0500)
(def invalid-value 0x0501)
(def invalid-operation 0x0502)
(def out-of-memory 0x0505)

; FrontFaceDirection
(def cw 0x0900)
(def ccw 0x0901)

; GetPName
(def line-width 0x0B21)
(def aliased-point-size-range 0x846D)
(def aliased-line-width-range 0x846E)
(def cull-face-mode 0x0B45)
(def front-face 0x0B46)
(def depth-range 0x0B70)
(def depth-writemask 0x0B72)
(def depth-clear-value 0x0B73)
(def depth-func 0x0B74)
(def stencil-clear-value 0x0B91)
(def stencil-func 0x0B92)
(def stencil-fail 0x0B94)
(def stencil-pass-depth-fail 0x0B95)
(def stencil-pass-depth-pass 0x0B96)
(def stencil-ref 0x0B97)
(def stencil-value-mask 0x0B93)
(def stencil-writemask 0x0B98)
(def stencil-back-func 0x8800)
(def stencil-back-fail 0x8801)
(def stencil-back-pass-depth-fail 0x8802)
(def stencil-back-pass-depth-pass 0x8803)
(def stencil-back-ref 0x8CA3)
(def stencil-back-value-mask 0x8CA4)
(def stencil-back-writemask 0x8CA5)
(def viewport 0x0BA2)
(def scissor-box 0x0C10)
(def color-clear-value 0x0C22)
(def color-writemask 0x0C23)
(def unpack-alignment 0x0CF5)
(def pack-alignment 0x0D05)
(def max-texture-size 0x0D33)
(def max-viewport-dims 0x0D3A)
(def subpixel-bits 0x0D50)
(def red-bits 0x0D52)
(def green-bits 0x0D53)
(def blue-bits 0x0D54)
(def alpha-bits 0x0D55)
(def depth-bits 0x0D56)
(def stencil-bits 0x0D57)
(def polygon-offset-units 0x2A00)
(def polygon-offset-factor 0x8038)
(def texture-binding-2d 0x8069)
(def sample-buffers 0x80A8)
(def samples 0x80A9)
(def sample-coverage-value 0x80AA)
(def sample-coverage-invert 0x80AB)

(def compressed-texture-formats 0x86A3)


; HintMode
(def dont-care 0x1100)
(def nicest 0x1102)
(def fastest 0x1101)


; HintTarget
(def generate-mipmap-hint 0x8192)


; DataType
(def byte 0x1400)
(def unsigned-byte 0x1401)
(def short 0x1402)
(def unsigned-short 0x1403)
(def int 0x1404)
(def unsigned-int 0x1405)
(def float 0x1406)


; PixelFormat
(def depth-component 0x1902)
(def alpha 0x1906)
(def rgb 0x1907)
(def rgba 0x1908)
(def luminance 0x1909)
(def luminance-alpha 0x190A)


; PixelType
(def unsigned-short-4-4-4-4 0x8033)
(def unsigned-short-5-5-5-1 0x8034)
(def unsigned-short-5-6-5 0x8363)


; Shaders
(def fragment-shader 0x8B30)
(def vertex-shader 0x8B31)
(def max-vertex-attribs 0x8869)
(def max-vertex-uniform-vectors 0x8DFB)
(def max-varying-vectors 0x8DFC)
(def max-combined-texture-image-units 0x8B4D)
(def max-vertex-texture-image-units 0x8B4C)
(def max-texture-image-units 0x8872)
(def max-fragment-uniform-vectors 0x8DFD)
(def shader-type 0x8B4F)
(def delete-status 0x8B80)
(def link-status 0x8B82)
(def validate-status 0x8B83)
(def attached-shaders 0x8B85)
(def active-uniforms 0x8B86)
(def active-attributes 0x8B89)
(def shading-language-version 0x8B8C)
(def current-program 0x8B8D)


; StencilFunction
(def never 0x0200)
(def less 0x0201)
(def equal 0x0202)
(def lequal 0x0203)
(def greater 0x0204)
(def notequal 0x0205)
(def gequal 0x0206)
(def always 0x0207)


; StencilOp
(def keep 0x1E00)
(def replace 0x1E01)
(def incr 0x1E02)
(def decr 0x1E03)
(def invert 0x150A)
(def incr-wrap 0x8507)
(def decr-wrap 0x8508)


; StringName
(def vendor 0x1F00)
(def renderer 0x1F01)
(def version 0x1F02)


; TextureMagFilter
(def nearest 0x2600)
(def linear 0x2601)


; TextureMinFilter
(def nearest-mipmap-nearest 0x2700)
(def linear-mipmap-nearest 0x2701)
(def nearest-mipmap-linear 0x2702)
(def linear-mipmap-linear 0x2703)


; TextureParameterName
(def texture-mag-filter 0x2800)
(def texture-min-filter 0x2801)
(def texture-wrap-s 0x2802)
(def texture-wrap-t 0x2803)


; TextureTarget
(def texture-2d 0x0DE1)
(def texture 0x1702)
(def texture-cube-map 0x8513)
(def texture-binding-cube-map 0x8514)
(def texture-cube-map-positive-x 0x8515)
(def texture-cube-map-negative-x 0x8516)
(def texture-cube-map-positive-y 0x8517)
(def texture-cube-map-negative-y 0x8518)
(def texture-cube-map-positive-z 0x8519)
(def texture-cube-map-negative-z 0x851A)
(def max-cube-map-texture-size 0x851C)


; TextureUnit
(def texture0 0x84C0)
(def texture1 0x84C1)
(def texture2 0x84C2)
(def texture3 0x84C3)
(def texture4 0x84C4)
(def texture5 0x84C5)
(def texture6 0x84C6)
(def texture7 0x84C7)
(def texture8 0x84C8)
(def texture9 0x84C9)
(def texture10 0x84CA)
(def texture11 0x84CB)
(def texture12 0x84CC)
(def texture13 0x84CD)
(def texture14 0x84CE)
(def texture15 0x84CF)
(def texture16 0x84D0)
(def texture17 0x84D1)
(def texture18 0x84D2)
(def texture19 0x84D3)
(def texture20 0x84D4)
(def texture21 0x84D5)
(def texture22 0x84D6)
(def texture23 0x84D7)
(def texture24 0x84D8)
(def texture25 0x84D9)
(def texture26 0x84DA)
(def texture27 0x84DB)
(def texture28 0x84DC)
(def texture29 0x84DD)
(def texture30 0x84DE)
(def texture31 0x84DF)
(def active-texture 0x84E0)

; TextureWrapMode
(def repeat 0x2901)
(def clamp-to-edge 0x812F)
(def mirrored-repeat 0x8370)


; Uniform Types
(def float-vec2 0x8B50)
(def float-vec3 0x8B51)
(def float-vec4 0x8B52)
(def int-vec2 0x8B53)
(def int-vec3 0x8B54)
(def int-vec4 0x8B55)
(def bool 0x8B56)
(def bool-vec2 0x8B57)
(def bool-vec3 0x8B58)
(def bool-vec4 0x8B59)
(def float-mat2 0x8B5A)
(def float-mat3 0x8B5B)
(def float-mat4 0x8B5C)
(def sampler-2d 0x8B5E)
(def sampler-cube 0x8B60)


; Vertex Arrays
(def vertex-attrib-array-enabled 0x8622)
(def vertex-attrib-array-size 0x8623)
(def vertex-attrib-array-stride 0x8624)
(def vertex-attrib-array-type 0x8625)
(def vertex-attrib-array-normalized 0x886A)
(def vertex-attrib-array-pointer 0x8645)
(def vertex-attrib-array-buffer-binding 0x889F)


; Shader Source
(def compile-status 0x8B81)


; Shader Precision-Specified Types
(def low-float 0x8DF0)
(def medium-float 0x8DF1)
(def high-float 0x8DF2)
(def low-int 0x8DF3)
(def medium-int 0x8DF4)
(def high-int 0x8DF5)


; Framebuffer Object
(def framebuffer 0x8D40)
(def renderbuffer 0x8D41)
(def rgba4 0x8056)
(def rgb5-a1 0x8057)
(def rgb565 0x8D62)
(def depth-component16 0x81A5)
(def stencil-index 0x1901)
(def stencil-index8 0x8D48)
(def depth-stencil 0x84F9)
(def renderbuffer-width 0x8D42)
(def renderbuffer-height 0x8D43)
(def renderbuffer-internal-format 0x8D44)
(def renderbuffer-red-size 0x8D50)
(def renderbuffer-green-size 0x8D51)
(def renderbuffer-blue-size 0x8D52)
(def renderbuffer-alpha-size 0x8D53)
(def renderbuffer-depth-size 0x8D54)
(def renderbuffer-stencil-size 0x8D55)
(def framebuffer-attachment-object-type 0x8CD0)
(def framebuffer-attachment-object-name 0x8CD1)
(def framebuffer-attachment-texture-level 0x8CD2)
(def framebuffer-attachment-texture-cube-map-face 0x8CD3)
(def color-attachment0 0x8CE0)
(def depth-attachment 0x8D00)
(def stencil-attachment 0x8D20)
(def depth-stencil-attachment 0x821A)
(def none 0)
(def framebuffer-complete 0x8CD5)
(def framebuffer-incomplete-attachment 0x8CD6)
(def framebuffer-incomplete-missing-attachment 0x8CD7)
(def framebuffer-incomplete-dimensions 0x8CD9)
(def framebuffer-unsupported 0x8CDD)
(def framebuffer-binding 0x8CA6)
(def renderbuffer-binding 0x8CA7)
(def max-renderbuffer-size 0x84E8)
(def invalid-framebuffer-operation 0x0506)


; WebGL-specific enums
(def unpack-flip-y-webgl 0x9240)
(def unpack-premultiply-alpha-webgl 0x9241)
(def context-lost-webgl 0x9242)
(def unpack-colorspace-conversion-webgl 0x9243)
(def browser-default-webgl 0x9244)
