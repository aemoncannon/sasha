;; Set buffer local settings
(setq scala-build-command 
      (list "ruby" (list (concat "-C" project-helper-project-root-dir)
			 "-e" "system \"rake.bat\"")))

(setq project-helper-summary-list
      `(("Project name" "scala anim")
	("Author" "Aemon Cannon")))

(defun scala-anim-compile ()
  "Launch an emacs compile for the current project"
  (interactive)
  (let ((command "rake.bat compile_and_run"))
    (compilation-start command)))
(define-key scala-mode-map (kbd "C-c k") 'scala-anim-compile)