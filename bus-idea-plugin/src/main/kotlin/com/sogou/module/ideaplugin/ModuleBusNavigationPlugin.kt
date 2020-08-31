package com.sogou.module.ideaplugin

import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.navigation.NavigationItem
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedMembersSearch
import java.awt.event.MouseEvent


class ModuleBusNavigationPlugin : LineMarkerProviderDescriptor(), GutterIconNavigationHandler<PsiElement> {

    var logger = Logger.getInstance(ModuleBusNavigationPlugin.javaClass)

    override fun getName(): String? {
        return "ModuleBus Navi"
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        return if (isNavigationCall(element)) {
//            logger.warn("getLineMarkerInfo : " + element.textRange)
            LineMarkerInfo<PsiElement>(element, element.textRange, navigationOnIcon,
                    Pass.UPDATE_ALL, null, this,
                    GutterIconRenderer.Alignment.LEFT)
        } else {
            null
        }
    }

    override fun navigate(e: MouseEvent?, psiElement: PsiElement?) {
        if (psiElement is PsiMethodCallExpression) {
            val psiExpressionList = (psiElement as PsiMethodCallExpressionImpl).argumentList
            if (psiExpressionList.expressions.size == 1) {

                val targetPath = psiExpressionList.expressions[0].text.replace("\"", "")
                logger.warn("targetPath : " + targetPath)
                val fullScope = GlobalSearchScope.allScope(psiElement.project)
                val routeAnnotationWrapper = AnnotatedMembersSearch.search(getAnnotationWrapper(psiElement, fullScope)
                        ?: return, fullScope).findAll()

                val target = routeAnnotationWrapper.find {
                    val list = it.modifierList?.annotations?.map {
                        it.findAttributeValue(ATTRIBUTE_VALUE)?.text?.replace("\"", "")
                    }

                    var desList = mutableListOf<String>()
                    list?.forEach {
                        logger.warn("item: " + it)
                        if (it!!.contains(strip(targetPath))){
                            desList.add(it)
                        }
                    }
                    desList.isNotEmpty()
                }

                logger.warn("target : " + target)
                if (null != target) {
                    // Redirect to target.
                    NavigationItem::class.java.cast(target).navigate(true)
                    return
                }
            }
        }

        notifyNotFound()
    }

    private fun notifyNotFound() {
        Notifications.Bus.notify(Notification(NOTIFY_SERVICE_NAME, NOTIFY_TITLE, NOTIFY_NO_TARGET_TIPS, NotificationType.WARNING))
    }

    private fun getAnnotationWrapper(psiElement: PsiElement?, scope: GlobalSearchScope): PsiClass? {
        if (null == routeAnnotationWrapper) {
            routeAnnotationWrapper = JavaPsiFacade.getInstance(psiElement?.project).findClass(ROUTE_ANNOTATION_NAME, scope)
        }

        return routeAnnotationWrapper
    }

    override fun collectSlowLineMarkers(elements: MutableList<PsiElement>, result: MutableCollection<LineMarkerInfo<PsiElement>>) {}

    /**
     * Judge whether the code used for navigation.
     */
    private fun isNavigationCall(psiElement: PsiElement): Boolean {
        if (psiElement is PsiCallExpression) {
            val method = psiElement.resolveMethod() ?: return false
            val parent = method.parent

//            logger.warn("isNavigationCall : " + method)
//            logger.warn("isNavigationCall : " + parent)
            if (method.name == MARKER_FUNCTION && parent is PsiClass) {
                if (isClassOfARouter(parent)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Judge whether the caller was ARouter
     */
    private fun isClassOfARouter(psiClass: PsiClass): Boolean {
//        logger.warn("isClassOfARouter : " + psiClass.getName())
        if (psiClass.name.equals(SDK_NAME)) {
            return true
        }

        // It super class was ARouter
        psiClass.supers.find { it.name == SDK_NAME } ?: return false

        return true
    }

    private fun strip(schema: String): String {
        if (schema.isEmpty()) return schema

        val split = schema.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (split.size >= 1) {
            split[0]
        } else schema

    }

    companion object {
        const val ROUTE_ANNOTATION_NAME = "com.sogou.annotation.RouterSchema"
        const val ATTRIBUTE_VALUE = "value"
        const val SDK_NAME = "RouterBus"
        const val MARKER_FUNCTION = "build"

        // Notify
        const val NOTIFY_SERVICE_NAME = "ModuleBus Plugin Tips"
        const val NOTIFY_TITLE = "ModuleBus Toast"
        const val NOTIFY_NO_TARGET_TIPS = "No destination found or unsupported type."

        val navigationOnIcon = IconLoader.getIcon("/icon/navi.png")
    }

    // I'm 100% sure this point can not made memory leak.
    private var routeAnnotationWrapper: PsiClass? = null
}