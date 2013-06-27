(ns gl)

((fn []
   (let [default-attributes {:alpha true
                             :depth true,
                             :stencil false,
                             :antialias true,
                             :premultiplied-alpha true,
                             :preserve-drawing-buffer false}
         attributes->js (fn 
                          [attributes]
                          (clj->js {
                                     :alpha (get attributes :alpha),
                                     :depth (get attributes :depth),
                                     :stencil (get attributes :stencil),
                                     :antialias (get attributes :antialias),
                                     :premultipliedAplha (get attributes :premultiplied-alpha),
                                     :preserveDrawingBuffer (get attributes :preserve-drawing-buffer)
                                     }))]
     
     (defmulti get-context (fn [& arglist] (count arglist)))
     
     (defmethod get-context 1
       [canvas-element]
       (.getContext canvas-element "experimental-webgl"))

     (defmethod get-context 2 
       [canvas-element context-attributes]
       (.getContext canvas-element "experimental-webgl" (attributes->js (merge default-attributes context-attributes))))
     )))

(defn get-context-attributes
  [gl-context]
  (let [js-obj (.getContextAttributes gl-context)]
    {
     :alpha (.-alpha js-obj),
     :depth (.-depth js-obj),
     :stencil (.-stencil js-obj),
     :antialias (.-antialias js-obj),
     :premultiplied-alpha (.-premultipliedAlpha js-obj),
     :preserve-drawing-buffer (.-preserveDrawingBuffer js-obj)
     }))
