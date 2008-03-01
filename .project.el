;; Set buffer local settings
(setq scala-build-command 
      (list "ruby" (list (concat "-C" project-helper-project-root-dir)
			 "-e" "system \"rake.bat\"")))

(setq project-helper-summary-list
      `(("Project name" "scala anim")
	("Author" "Aemon Cannon")))

