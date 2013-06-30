(ns cljswebgl.gl)
  
(defn get-context
  
  ([canvas-element]
     (.getContext canvas-element "experimental-webgl"))
  
  ([canvas-element context-attributes]
     (let [default-attributes {:alpha true
                               :depth true,
                               :stencil false,
                               :antialias true,
                               :premultiplied-alpha true,
                               :preserve-drawing-buffer false}
           attributes->js (fn
                            [{alpha :alpha depth :depth stencil :stencil antialias :antialias
                              premultiplied-alpha :premultiplied-alpha preserve-drawing-buffer :preserve-drawing-buffer}]
                            (clj->js {:alpha alpha,
                                      :depth depth,
                                      :stencil stencil,
                                      :antialias antialias,
                                      :premultipliedAplha premultiplied-alpha,
                                      :preserveDrawingBuffer preserve-drawing-buffer}))]
       (.getContext canvas-element "experimental-webgl" (attributes->js (merge default-attributes context-attributes))))))

(defn get-context-attributes
  [gl-context]
  (let [js-obj (.getContextAttributes gl-context)]
    {:alpha (.-alpha js-obj),
     :depth (.-depth js-obj),
     :stencil (.-stencil js-obj),
     :antialias (.-antialias js-obj),
     :premultiplied-alpha (.-premultipliedAlpha js-obj),
     :preserve-drawing-buffer (.-preserveDrawingBuffer js-obj)
     }))
