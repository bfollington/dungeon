(ns dungeon.core
  (:require [uix.core :refer [defui $ use-state]]
            ["@react-three/fiber" :refer [Canvas useFrame]]
            ["@react-three/drei" :refer [Box PointLight useTexture PerspectiveCamera OrbitControls]]
            ["react" :refer [useRef Suspense]]
            ["three" :refer [NearestFilter]]
            [applied-science.js-interop :as j]
            [uix.dom]))

(defui button [{:keys [on-click children]}]
  ($ :button.btn {:on-click on-click}
     children))

(def brick-url "bricks.png")

(defui BrickBox [{:keys [position]}]
  (let [texture (useTexture brick-url)]
    (j/assoc! texture :magFilter NearestFilter)
    (j/assoc! texture :minFilter NearestFilter)

    ($ Box {:position position}
       ($ :meshStandardMaterial {:map texture}))))

(defui tunnel []
  ($ :<>
     ($ BrickBox {:position (j/lit [-1 0 0])})
     ($ BrickBox {:position (j/lit [-1 0 -1])})
     ($ BrickBox {:position (j/lit [1 0 0])})
     ($ BrickBox {:position (j/lit [1 0 -1])})
     ($ BrickBox {:position (j/lit [0 -1 0])})
     ($ BrickBox {:position (j/lit [0 -1 -1])})
     ($ BrickBox {:position (j/lit [0 1 0])})
     ($ BrickBox {:position (j/lit [0 1 -1])})))

(defui canvas []
  ($ Canvas {}
     ($ Suspense {:fallback nil}
        ($ :pointLight {:position (j/lit [0 1 1])})
        ($ tunnel)
        ($ PerspectiveCamera {:position (j/lit [0 0 1.5]) :make-default true})
        ($ OrbitControls {:make-default true}))))

(defui app []
  (let [[state set-state!] (use-state 0)]
    ($ :<>
       ($ canvas)
       ($ button {:on-click #(set-state! dec)} "-")
       ($ :span state)
       ($ button {:on-click #(set-state! inc)} "+"))))

(defn init []
  (uix.dom/render ($ app) (js/document.getElementById "app")))
