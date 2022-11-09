(ns donut.minimal.frontend.ui
  (:require
   [lambdaisland.ornament :as o]))

(o/defstyled ul :div
  :my-3
  {"--gi-divide-y-reverse" 0}
  [:>.list-container :bg-white :shadow :overflow-hidden :sm:rounded-md]
  [:>.list-container>ul :divide-y :divide-gray-200]
  [:a :block :p-3]
  ([list-items]
   [:<>
    (into
     [:div.list-container
      ;; todo what's the unwraping way to do this?
      (->> list-items
           (map (fn [li] [:li li]))
           (into [:ul {:role "list" :class "divide-y"}]))])]))

(o/defstyled link :span
  [:>a :text-blue-600 :hover:text-green-600 :hover:bg-gray-50]
  ([link]
   [:<> link]))

(o/defstyled form :div
  [:input
   :shadow-sm
   :focus:ring-indigo-500
   :focus:border-indigo-500
   :block
   :w-full
   :sm:text-sm
   :border-gray-300
   :rounded-md
   :py-2
   :px-3
   ]
  ([& children]
   (into [:<>] children)))

(o/defstyled h1 :h1
  :order-1
  :text-gray-900
  :text-3xl
  :font-extrabold
  :tracking-tight
  :mt-2
  ([& children]
   (into [:<>] children)))

(o/defstyled h2 :h2
  :order-1
  :text-gray-900
  :text-2xl
  :font-bold
  :tracking-tight
  :mt-2
  ([& children]
   (into [:<>] children)))
