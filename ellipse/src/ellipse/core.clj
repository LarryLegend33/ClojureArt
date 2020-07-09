;; think this over a bit. may want to try cos x 

(ns ellipse.core
  (:require [genartlib.random :as gen]
            [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]))

; sqrt( b2 (1 - x2/a2))
(def envwidth 1000)
(def envheight 560)

(defn ellipse_path
  [a b]
  (fn [t]
    (math/sqrt (* (math/expt b 2)
                  (- 1 (/ (math/expt t 2) (math/expt a 2)))))))

(defn setup []
  (q/frame-rate 30)
  {:ellipse_path (ellipse_path (/ envwidth 4) (/ envheight 6))
   :epoch 1
   :translation_rate 4
   :center_x (* envwidth 0.25)
   :center_y (/ envheight 2)})

(defn update-state [state]
  (q/frame-rate 30)
  (print (:center_x state))
  (let [inc_x (+ (:center_x state) (:translation_rate state))
        dec_x (- (:center_x state) (:translation_rate state))]
    {:ellipse_path (:ellipse_path state)
     :translation_rate (:translation_rate state)
                                        ; change epoch function to get noises to switch faster
                                        ; have it be a mod over envwidth
     :center_x (cond (< (:center_x state) (* 0.25 envwidth))
                     inc_x
                     (> (:center_x state) (* 0.75 envwidth))
                     dec_x
                     (odd? (:epoch state))
                     inc_x
                     (even? (:epoch state))
                     dec_x)
     
     :epoch (cond (> (:center_x state) (* 0.75 envwidth))
                  (+ 1 (:epoch state))
                  (< (:center_x state) (* 0.25 envwidth))
                  (+ 1 (:epoch state))
                  :else (:epoch state))
     
     :center_y (:center_y state)}))


(defn gen_offset [epoch noiselevel dot_index]
  (gen/gauss 0 noiselevel))

(def offset_mem (memoize gen_offset))

(defn draw-state [state]
  (q/background 0)
  (q/fill 255 255 255)
  (q/stroke 255 255 255)
  (let [edge_noise 0
        center_noise 0
        global_noise 0
        y ((:ellipse_path state) (- (:center_x state) (/ envwidth 2)))]
    (q/with-translation [(gen/gauss (:center_x state) 0)
                         (gen/gauss (:center_y state) 0)]
      (q/ellipse (gen/gauss 0 edge_noise) (/ envheight 6) 10 10)
      (q/ellipse (gen/gauss 0 edge_noise) (* -1 (/ envheight 6)) 10 10)
;      (q/ellipse (gen/gauss 0 center_noise) y 10 10)
      (q/ellipse (gen/gauss 0 center_noise)
                 (cond (odd? (:epoch state))
                       y
                       :else (* -1 y))
                 10 10)
    ;  (q/ellipse (offset_mem (:epoch state) center_noise 0) y 10 10)
      )))

(q/defsketch ellipse
  :title "elliptical motion illusion"
  :size [envwidth envheight]
  :setup setup
  :draw draw-state
  :update update-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])




;; calibrating stimuli well.
;; monte carlo inference over structured scene graphs right.
;; prior knowledge enumerated crosscat strucxtures, gridded all numerical params
;; check samplers converge to same thing as grid approximations. billions.
;; getting at truth of the structure. team together to fund.
;; how do we perceive structure and form. auditory cues change percept. 
;; simple perception of 3D structure from motion.
;; use austin stimuli to show them what we're thinking.
;; make space for them to have ideas.

;; 
;; 2nd facet is structure from point light displays
;; vikash has a PPL written by marco, austin demo.
;; vikash is planning to reach out to you to talk about it 
;; once we are further along.
;; 
;; lab space, training a student. impossible at the moment. 
;; happy to keep having conversations about it. happy to meet with a
;; student to get them up and running.
;; earliest possible point that you could reach on the heat project.

;; mo khalil, jim collins
;; jeremy guodina
;; beatrice gelber
;; protozoa pellets
