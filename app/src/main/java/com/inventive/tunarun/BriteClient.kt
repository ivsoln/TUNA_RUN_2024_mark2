package com.inventive.tunarun

import android.content.Context

class BriteClient constructor(ctx: Context) {
    private var baseUrl = "http://172.32.0.55/IVSoln.Tuna.RESTful/"
    private var connect: Connect = Connect(ctx)
}

