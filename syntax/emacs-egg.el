(defvar egg-constants
  '("true"
    "false"
    "nil"))

(defvar egg-builtins
  '("option" "->" "space" "sugar" "term" "call" "write" "new"))

(defvar egg-keywords
  '("inh" "syn" "is" "for" "do" "end" "match" "with" "global" "local"))

(defvar egg-tab-width 4 "Width of a tab for EGG mode")

(defvar egg-font-lock-defaults
  `((
     ("\"\\.\\*\\?" . font-lock-string-face) ;; stuff between ""
     ("\\_<[0-9]+\\_>" . font-lock-string-face) ;; numbers
     ("#[a-zA-Z0-9_]+" . font-lock-variable-name-face) ;; egg actions
     (": ?\\([a-zA-Z0-9_]+\\)" 1 font-lock-type-face) ;; types
     ( ,(regexp-opt egg-keywords 'words) . font-lock-keyword-face)
     ( ,(regexp-opt egg-builtins 'words) . font-lock-builtin-face)
     ( ,(regexp-opt egg-constants 'words) . font-lock-constant-face)
     )))

(defvar egg_idnent-line-function

  )

(define-derived-mode egg-mode fundamental-mode "EGG script"
  "EGG mode is a major mode for editing EGG files"
  (setq font-lock-defaults egg-font-lock-defaults)
  (setq indent-line-function egg-indent-line-function)

  (when egg-tab-width
    (setq tab-width egg-tab-width))

  ;; -- comments
  (setq comment-start "--")
  (setq comment-end "")
  (modify-syntax-entry ?- ". 124b" egg-mode-syntax-table)
  (modify-syntax-entry ?\n "> b" egg-mode-syntax-table)
  )

(provide 'egg-mode)
