package com.arkhamcards.v2.domain.objects

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ImmutableStringSetSerializer :
    KSerializer<ImmutableSet<String>> {

    private val delegate = SetSerializer(String.serializer())

    override val descriptor = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: ImmutableSet<String>
    ) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(
        decoder: Decoder
    ): ImmutableSet<String> =
        delegate.deserialize(decoder).toPersistentSet()
}