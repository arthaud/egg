;; To use this file:
;; * put it somewhere in your load path
;; * require the mode (the mode and filename must correspond
;;
;; For example, in my case:
;; (add-to-list 'load-path "~/.emacs.d/")
;; (require 'egg-mode)


(defvar egg-constants
  '("true" "false" "nil" "null"))

(defvar egg-builtins
  '("option" "->" "space" "sugar" "term" "call" "write" "new"))

(defvar egg-keywords
  '("inh" "syn" "is" "for" "do" "end" "match" "with" "global" "local" "if" "else"))

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

(defun egg-indent-line()
  "indent current line as EGG code"
  (beginning-of-line)
  (if (bobp) ;; no indentation at beginning of file
      (indent-line-to 0)
    (let ((not-indented t) cur-indent)
      (if (looking-at "^[ \t]*\\(else\\|end\\|#\\|}\\)") ;; one less indentation level than above line
          (progn
            (save-excursion
              (forward-line -1)
              (setq cur-indent (- (current-indentation) egg-tab-width)))
            (if (< cur-indent 0)
                (setq cur-indent 0)))
        (save-excursion
          (while not-indented
            (forward-line -1)
            (if (looking-at "^[ \t]*\\(end\\|}\\)") ;; if upper line is end or }, then same indentation as this line
                (progn
                  (setq cur-indent (current-indentation))
                  (setq not-indented nil))
              (if (looking-at "\\(^[ \t]*\\(global\\|local\\|do\\|if\\|else\\|#\\)\\|{$\\)") ;; if upper line is one of these keywords, add an indentation level
                  (progn
                    (setq cur-indent (+ (current-indentation) egg-tab-width))
                    (setq not-indented nil))
                (if (bobp)
                    (setq not-indented nil)))))))
      (if cur-indent
          (indent-line-to cur-indent)
        (indent-line-to 0))))) ; If we didn't see an indentation hint, then allow no indentation

(define-derived-mode egg-mode fundamental-mode "EGG script"
  "EGG mode is a major mode for editing EGG files"
  (setq font-lock-defaults egg-font-lock-defaults)
  (setq indent-line-function 'egg-indent-line)

  (when egg-tab-width
    (setq tab-width egg-tab-width))

  ;; -- comments
  (setq comment-start "--")
  (setq comment-end "")
  (modify-syntax-entry ?- ". 124b" egg-mode-syntax-table)
  (modify-syntax-entry ?\n "> b" egg-mode-syntax-table)
  )

(add-to-list 'auto-mode-alist '("\\.egg\\'" . egg-mode))
(provide 'egg-mode)
